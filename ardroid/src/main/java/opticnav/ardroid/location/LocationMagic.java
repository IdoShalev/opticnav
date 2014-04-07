package opticnav.ardroid.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import opticnav.ardd.protocol.GeoCoordFine;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class LocationMagic {
    private static XLogger LOG = XLoggerFactory.getXLogger(LocationMagic.class);

    public interface Listener {
        /**
         * Called when location updates become noticeably responsive or unresponsive.
         * "Responsive" is considered to be at most once every 2 seconds.
         * This is only called when responsiveness changes.
         *
         * @param good True if responsiveness is good, false if not
         */
        public void responsivenessUpdate(boolean good);
        public void locationUpdate(GeoCoordFine geoCoord);
    }

    private final LocationManager locationManager;
    private LocationListener locationListener;

    public LocationMagic(LocationManager locationManager) {
        this.locationManager = locationManager;
        this.locationListener = null;
    }

    public void setListener(final Listener listener) {
        if (this.locationListener != null) {
            throw new IllegalStateException("Listener is already set");
        }

        this.locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                LOG.debug(location.toString());

                final GeoCoordFine geoCoord;
                geoCoord = GeoCoordFine.fromDouble(location.getLongitude(), location.getLatitude());
                listener.locationUpdate(geoCoord);
            }
        };

        LOG.info("LocationMagic listener set");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this.locationListener);
    }

    public void close() {
        if (this.locationListener != null) {
            this.locationManager.removeUpdates(this.locationListener);
            LOG.info("LocationMagic closed");
        }
    }
}