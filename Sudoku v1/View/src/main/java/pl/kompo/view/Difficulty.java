package pl.kompo.view;


public enum Difficulty {
    EASY(1, "Easy"),
    MEDIUM(40, "Medium"),
    HARD(60, "Hard");

    private final int fieldsToRemove;
    private final String levelName;

    Difficulty(int fieldsToRemove, String levelName) {
        this.fieldsToRemove = fieldsToRemove;
        this.levelName = levelName;
    }

    public int getFieldsToRemove() {
        return fieldsToRemove;
    }

    public String getLevelName() {
        return levelName;
    }
}
