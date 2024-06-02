package carpetlabaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpetlabaddition.utils.CarpetLABAdditionTranslations;
import net.fabricmc.api.ModInitializer;

import java.util.Map;

public class LABServer implements CarpetExtension, ModInitializer {
    @Override
    public String version() {
        return "carpet-lab-addition";
    }

    public static void loadExtension() {
        CarpetServer.manageExtension(new LABServer());
    }

    @Override
    public void onInitialize() {
        LABServer.loadExtension();
    }

    @Override
    public void onGameStarted() {
        LABEvents.noop();
        CarpetServer.settingsManager.parseSettingsClass(LABSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetLABAdditionTranslations.getTranslationFromResourcePath(lang);
    }
}
