package opticnav.daemon.device;

import opticnav.daemon.protocol.GeoCoordFine;

/**
 * The MapTransform object is responsible for converting geocoordinates to image-local coordinates, and vice-versa.
 * 
 * MapTransform is implemented as a 3x2 matrix transform.
 * 
 * @author Danny Spencer
 *
 */
public final class MapTransform {
    public static final class Anchor {
        public final int localX, localY;
        public final GeoCoordFine geoCoord;

        public Anchor(int localX, int localY, GeoCoordFine geoCoord) {
            this.localX = localX;
            this.localY = localY;
            this.geoCoord = geoCoord;
        }
    }

    public static final class Coordinate {
        public final double x, y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private final double m00,m01,m02,
                         m10,m11,m12;

    public MapTransform(Anchor p0, Anchor p1, Anchor p2) {
        final double px0, py0, px1, py1, px2, py2;
        final double gx0, gy0, gx1, gy1, gx2, gy2;

        px0 = p0.localX;
        py0 = p0.localY;
        px1 = p1.localX;
        py1 = p1.localY;
        px2 = p2.localX;
        py2 = p2.localY;

        gx0 = p0.geoCoord.getLongitudeInt();
        gy0 = p0.geoCoord.getLatitudeInt();
        gx1 = p1.geoCoord.getLongitudeInt();
        gy1 = p1.geoCoord.getLatitudeInt();
        gx2 = p2.geoCoord.getLongitudeInt();
        gy2 = p2.geoCoord.getLatitudeInt();

        // Some very complicated formulas churned out by wxMaxima.
        /*
            linsolve([
            gx0 = m00*px0 + m01*py0 + m02,
            gy0 = m10*px0 + m11*py0 + m12,

            gx1 = m00*px1 + m01*py1 + m02,
            gy1 = m10*px1 + m11*py1 + m12,

            gx2 = m00*px2 + m01*py2 + m02,
            gy2 = m10*px2 + m11*py2 + m12
            ],
            [m00, m01, m02, m10, m11, m12]);
        */
        m00=(gx0*(py2-py1)-gx1*py2+gx2*py1+(gx1-gx2)*py0)/(px0*(py2-py1)-px1*py2+px2*py1+(px1-px2)*py0);
        m01=-(gx0*(px2-px1)-gx1*px2+gx2*px1+(gx1-gx2)*px0)/(px0*(py2-py1)-px1*py2+px2*py1+(px1-px2)*py0);
        m02=(gx0*(px2*py1-px1*py2)+px0*(gx1*py2-gx2*py1)+(gx2*px1-gx1*px2)*py0)/(px0*(py2-py1)-px1*py2+px2*py1+(px1-px2)*py0);
        m10=(gy0*(py2-py1)-gy1*py2+gy2*py1+(gy1-gy2)*py0)/(px0*(py2-py1)-px1*py2+px2*py1+(px1-px2)*py0);
        m11=-(gy0*(px2-px1)-gy1*px2+gy2*px1+(gy1-gy2)*px0)/(px0*(py2-py1)-px1*py2+px2*py1+(px1-px2)*py0);
        m12=(gy0*(px2*py1-px1*py2)+px0*(gy1*py2-gy2*py1)+(gy2*px1-gy1*px2)*py0)/(px0*(py2-py1)-px1*py2+px2*py1+(px1-px2)*py0);
    }
    
    public GeoCoordFine imageLocalToGeo(double x, double y) {
        final double lat = m00*x + m01*y + m02;
        final double lng = m10*x + m11*y + m12;
        return new GeoCoordFine((int)Math.round(lat), (int)Math.round(lng));
    }

    public GeoCoordFine imageLocalToGeo(Coordinate coordinate) {
        return imageLocalToGeo(coordinate.x, coordinate.y);
    }

    public Coordinate geoToImageLocal(GeoCoordFine coordinate) {
        final double lng = coordinate.getLongitudeInt();
        final double lat = coordinate.getLatitudeInt();

        // More formulas churned out by wxMaxima
        // It's the inverse of a matrix multiplication
        final double x = (m01*(lat-m12)+m02*m11-lng*m11)/(m01*m10-m00*m11);
        final double y = -(m00*(lat-m12)+m02*m10-lng*m10)/(m01*m10-m00*m11);

        return new Coordinate(x, y);
    }
}
