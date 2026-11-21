package tn.esprit.ms3locationzone.common.util;

public final class GeoUtils {

    private static final double RAYON_TERRE_KM = 6371.0088;

    private GeoUtils() {
    }

    // Distance "à vol d'oiseau" entre deux points GPS (formule de Haversine)
    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return RAYON_TERRE_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public static void verifier(Double lat, Double lon) {
        if (lat == null || lon == null || lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Coordonnees GPS invalides (lat -90..90, lon -180..180)");
        }
    }
}