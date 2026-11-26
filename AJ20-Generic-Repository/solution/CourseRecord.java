record CourseRecord(String id, String title, int moduleCount, boolean active)
    implements Identified {
    private static final int MINIMUM_MODULE_COUNT = 0;
    private static final int LARGE_COURSE_MODULE_THRESHOLD = 10;

    public CourseRecord {
        // Require every course to have a usable id
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }

        // Require every course to have a readable title
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }

        // Reject impossible negative module counts
        if (moduleCount < MINIMUM_MODULE_COUNT) {
            throw new IllegalArgumentException(
                "module count cannot be negative");
        }
    }

    /**
     * @brief Check whether the course has many modules
     *
     * @return True when the course meets the large-course threshold
     */
    public boolean isLargeCourse() {
        return moduleCount >= LARGE_COURSE_MODULE_THRESHOLD;
    }
}
