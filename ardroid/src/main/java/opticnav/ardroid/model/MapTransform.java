package opticnav.ardroid.model;

public class MapTransform {
    private final double m00,m01,m02,
                         m10,m11,m12;

    public MapTransform(Anchor p0, Anchor p1, Anchor p2) {
        final double px0, py0, px1, py1, px2, py2;
        final double gx0, gy0, gx1, gy1, gx2, gy2;

        px0 = p0.getLocalX();
        py0 = p0.getLocalY();
        px1 = p1.getLocalX();
        py1 = p1.getLocalY();
        px2 = p2.getLocalX();
        py2 = p2.getLocalY();

        gx0 = p0.getGPSCoordinate().getLongitudeDouble();
        gy0 = p0.getGPSCoordinate().getLatitudeDouble();
        gx1 = p1.getGPSCoordinate().getLongitudeDouble();
        gy1 = p1.getGPSCoordinate().getLatitudeDouble();
        gx2 = p2.getGPSCoordinate().getLongitudeDouble();
        gy2 = p2.getGPSCoordinate().getLatitudeDouble();

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

    public Coordinate imageLocalToGPS(Coordinate coordinate) {
        final double x = coordinate.getX();
        final double y = coordinate.getY();

        final double lat = m00*x + m01*y + m02;
        final double lng = m10*x + m11*y + m12;
        return new Coordinate(lat, lng);
    }

    public Coordinate gpsToImageLocal(GPSCoordinate coordinate) {
        final double lat = coordinate.getLatitudeDouble();
        final double lng = coordinate.getLongitudeDouble();

        // More formulas churned out by wxMaxima
        // It's the inverse of a matrix multiplication
        final double x = (m01*(lat-m12)+m02*m11-lng*m11)/(m01*m10-m00*m11);
        final double y = -(m00*(lat-m12)+m02*m10-lng*m10)/(m01*m10-m00*m11);

        return new Coordinate(x, y);
    }
}
