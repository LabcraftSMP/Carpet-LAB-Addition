package carpetlabaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpetlabaddition.utils.CarpetLABAdditionTranslations;
import net.fabricmc.api.ModInitializer;

import java.util.Map;

public class CarpetLABAdditionServer implements CarpetExtension, ModInitializer {
    @Override
    public String version() {
        return "carpet-lab-addition";
    }

    public static void loadExtension() {
        CarpetServer.manageExtension(new CarpetLABAdditionServer());
    }

    @Override
    public void onInitialize() {
        CarpetLABAdditionServer.loadExtension();
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetLABAdditionSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetLABAdditionTranslations.getTranslationFromResourcePath(lang);
    }
}
