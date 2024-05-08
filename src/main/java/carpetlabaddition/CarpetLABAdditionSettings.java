package carpetlabaddition;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.*;

public class CarpetLABAdditionSettings {
    public static final String LAB = "LAB";

    @Rule(categories = {LAB, FEATURE})
    public static boolean hoppersDontStackShulkers = false;
}
