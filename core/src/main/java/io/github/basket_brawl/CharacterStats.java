package io.github.basket_brawl;

/** Holds character stats that affect gameplay. */
public class CharacterStats {
    public float speed;              // Walk speed multiplier (default 1.0f)
    public float threePointAccuracy; // 3-point shooting accuracy (0.0-1.0, affects green zone width)
    public float midRangeAccuracy;   // 2-point shooting accuracy (0.0-1.0, affects green zone width)
    public float jumpHeight;         // Jump/block height multiplier (default 1.0f)

    public CharacterStats(float speed, float threePointAccuracy, float midRangeAccuracy, float jumpHeight) {
        this.speed = speed;
        this.threePointAccuracy = threePointAccuracy;
        this.midRangeAccuracy = midRangeAccuracy;
        this.jumpHeight = jumpHeight;
    }

    public static CharacterStats getStatsForCharacter(String characterName) {
        if (characterName == null) {
            characterName = "Stepth Curry";
        }

        switch (characterName) {
            case "Stepth Curry":
                return new CharacterStats(1.0f, 0.85f, 0.70f, 0.9f);
            case "Kevin Durant":
                return new CharacterStats(0.95f, 0.80f, 0.75f, 0.95f);
            case "Lebron James":
                return new CharacterStats(0.90f, 0.70f, 0.85f, 1.1f);
            case "Jimmy Butler":
                return new CharacterStats(0.95f, 0.75f, 0.80f, 1.0f);
            case "Mavir":
                return new CharacterStats(1.05f, 0.75f, 0.78f, 0.95f);
            case "Brandon":
                return new CharacterStats(0.88f, 0.72f, 0.82f, 1.05f);
            case "Vishal":
                return new CharacterStats(0.92f, 0.73f, 0.80f, 1.0f);
            case "Jayson Tatum":
                return new CharacterStats(0.93f, 0.78f, 0.83f, 1.02f);
            default:
                return new CharacterStats(1.0f, 0.75f, 0.75f, 1.0f);
        }
    }
}
