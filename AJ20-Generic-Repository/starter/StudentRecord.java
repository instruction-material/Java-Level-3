record StudentRecord(String id, String name, int completedModules)
    implements Identified {
    private static final int MINIMUM_COMPLETED_MODULES = 0;

    public StudentRecord {
        // Require every student to have a usable id
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }

        // Require every student to have a readable name
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        // Reject impossible negative progress counts
        if (completedModules < MINIMUM_COMPLETED_MODULES) {
            throw new IllegalArgumentException(
                "completed modules cannot be negative");
        }
    }
}
