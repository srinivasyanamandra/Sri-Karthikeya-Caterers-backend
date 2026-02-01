package sri.karthikeya.caterers.engine.s3;

import sri.karthikeya.caterers.enums.GalleryType;

public final class S3PathConstants {
    
    private S3PathConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static String getPathByType(GalleryType type) {
        return switch (type) {
            case MENU -> "menu/";
            case SERVICES -> "services/";
            case TEAM -> "team/";
            case REVIEWS -> "reviews/";
            case GALLERY -> "gallery/";
        };
    }
}
