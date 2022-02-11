package de.photon.aacadditionpro.modules;

import de.photon.aacadditionpro.modules.additions.BrandHider;
import de.photon.aacadditionpro.modules.additions.DamageIndicator;
import de.photon.aacadditionpro.modules.additions.EnchantmentHider;
import de.photon.aacadditionpro.modules.additions.LogBot;
import de.photon.aacadditionpro.modules.additions.esp.Esp;
import de.photon.aacadditionpro.modules.checks.autoeat.AutoEat;
import de.photon.aacadditionpro.modules.checks.autofish.AutoFishConsistency;
import de.photon.aacadditionpro.modules.checks.autofish.AutoFishInhumanReaction;
import de.photon.aacadditionpro.modules.checks.autopotion.AutoPotion;
import de.photon.aacadditionpro.modules.checks.fastswitch.Fastswitch;
import de.photon.aacadditionpro.modules.checks.impossiblechat.ImpossibleChat;
import de.photon.aacadditionpro.modules.checks.inventory.InventoryAverageHeuristic;
import de.photon.aacadditionpro.modules.checks.inventory.InventoryHit;
import de.photon.aacadditionpro.modules.checks.inventory.InventoryMove;
import de.photon.aacadditionpro.modules.checks.inventory.InventoryMultiInteraction;
import de.photon.aacadditionpro.modules.checks.inventory.InventoryPerfectExit;
import de.photon.aacadditionpro.modules.checks.inventory.InventoryRotation;
import de.photon.aacadditionpro.modules.checks.inventory.InventorySprinting;
import de.photon.aacadditionpro.modules.checks.packetanalysis.PacketAnalysisAnimation;
import de.photon.aacadditionpro.modules.checks.packetanalysis.PacketAnalysisEqualRotation;
import de.photon.aacadditionpro.modules.checks.packetanalysis.PacketAnalysisIllegalPitch;
import de.photon.aacadditionpro.modules.checks.pingspoof.Pingspoof;
import de.photon.aacadditionpro.modules.checks.scaffold.Scaffold;
import de.photon.aacadditionpro.modules.checks.skinblinker.SkinBlinkerSprinting;
import de.photon.aacadditionpro.modules.checks.skinblinker.SkinBlinkerUnusedBit;
import de.photon.aacadditionpro.modules.checks.teaming.Teaming;
import de.photon.aacadditionpro.modules.checks.tower.Tower;
import de.photon.aacadditionpro.modules.sentinel.BetterSprintingSentinel;
import de.photon.aacadditionpro.modules.sentinel.FiveZigSentinel;
import de.photon.aacadditionpro.modules.sentinel.LabyModSentinel;
import de.photon.aacadditionpro.modules.sentinel.SchematicaSentinel;
import de.photon.aacadditionpro.modules.sentinel.SentinelChannelModule;
import de.photon.aacadditionpro.modules.sentinel.VapeSentinel;
import de.photon.aacadditionpro.modules.sentinel.WorldDownloaderSentinel;
import de.photon.aacadditionpro.modules.sentinel.exploits.CommandBlockSentinel;
import de.photon.aacadditionpro.modules.sentinel.exploits.CreativeKillPotionSentinel;
import de.photon.aacadditionpro.modules.sentinel.exploits.SelfDamageSentinel;
import de.photon.aacadditionpro.modules.sentinel.exploits.TrollPotionSentinel;
import de.photon.aacadditionpro.util.config.ConfigUtils;
import de.photon.aacadditionpro.util.datastructure.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.konsolas.aac.api.AACCustomFeature;
import me.konsolas.aac.api.AACCustomFeatureProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleManager
{
    @Getter private static final ModuleMap<Module> moduleMap;
    @Getter private static final ModuleMap<ViolationModule> violationModuleMap;

    static {
        // Additions
        val brandHider = BrandHider.INSTANCE;
        val damageIndicator = new DamageIndicator();
        val enchantmentHider = new EnchantmentHider();
        val esp = new Esp();
        val logBot = new LogBot();

        // Checks
        val autoEat = new AutoEat();

        val autoFishConsistency = new AutoFishConsistency();
        val autoFishInhumanReaction = new AutoFishInhumanReaction();
        val autoFish = ViolationModule.parentOf("AutoFish", autoFishConsistency, autoFishInhumanReaction);

        val autoPotion = new AutoPotion();

        val fastswitch = new Fastswitch();

        val impossibleChat = new ImpossibleChat();

        val inventoryAverageHeuristic = new InventoryAverageHeuristic();
        val inventoryHit = new InventoryHit();
        val inventoryMove = new InventoryMove();
        val inventoryMultiInteraction = new InventoryMultiInteraction();
        val inventoryPerfectExit = new InventoryPerfectExit();
        val inventoryRotation = new InventoryRotation();
        val inventorySprinting = new InventorySprinting();
        val inventory = ViolationModule.parentOf("Inventory", inventoryAverageHeuristic, inventoryHit, inventoryMove, inventoryMultiInteraction, inventoryPerfectExit, inventoryRotation, inventorySprinting);


        val packetAnalysisAnimation = new PacketAnalysisAnimation();
        val packetAnalysisEqualRotation = new PacketAnalysisEqualRotation();
        val packetAnalysisIllegalPitch = new PacketAnalysisIllegalPitch();
        val packetAnalysis = ViolationModule.parentOf("PacketAnalysis", packetAnalysisAnimation, packetAnalysisEqualRotation, packetAnalysisIllegalPitch);

        val pingspoof = new Pingspoof();

        val scaffold = new Scaffold();

        val skinBlinkerUnusedBit = new SkinBlinkerUnusedBit();
        val skinBlinkerSprinting = new SkinBlinkerSprinting();
        val skinBlinker = ViolationModule.parentOf("Skinblinker", skinBlinkerUnusedBit, skinBlinkerSprinting);

        val teaming = new Teaming();

        val tower = new Tower();

        // Sentinel
        val commandBlockSentinel = new CommandBlockSentinel();
        val creativeKillPotionSentinel = new CreativeKillPotionSentinel();
        val selfDamageSentinel = new SelfDamageSentinel();
        val trollPotionSentinel = new TrollPotionSentinel();

        val betterSprintingSentinel = new BetterSprintingSentinel();
        val fiveZigSentinel = new FiveZigSentinel();
        val labyModSentinel = new LabyModSentinel();
        val schematicaSentinel = new SchematicaSentinel();
        val vapeSentinel = new VapeSentinel();
        val worldDownloaderSentinel = new WorldDownloaderSentinel();

        val moduleList = new ArrayList<>(Arrays.asList(
                // Additions
                brandHider,
                damageIndicator,
                enchantmentHider,
                esp,
                logBot,

                // Checks
                autoEat,

                autoFishConsistency,
                autoFishInhumanReaction,
                autoFish,

                autoPotion,

                fastswitch,

                impossibleChat,

                inventoryAverageHeuristic,
                inventoryHit,
                inventoryMove,
                inventoryMultiInteraction,
                inventoryPerfectExit,
                inventoryRotation,
                inventorySprinting,
                inventory,

                packetAnalysisAnimation,
                packetAnalysisEqualRotation,
                packetAnalysisIllegalPitch,
                packetAnalysis,

                pingspoof,

                scaffold.getScaffoldAngle(),
                scaffold.getScaffoldJumping(),
                scaffold.getScaffoldPosition(),
                scaffold.getScaffoldRotationDerivative(),
                scaffold.getScaffoldRotationFastChange(),
                scaffold.getScaffoldRotationSecondDerivative(),
                scaffold.getScaffoldSafewalkPosition(),
                scaffold.getScaffoldSafewalkTiming(),
                scaffold.getScaffoldSprinting(),
                scaffold,

                skinBlinkerUnusedBit,
                skinBlinkerSprinting,
                skinBlinker,

                teaming,

                tower,

                // Sentinel
                commandBlockSentinel,
                creativeKillPotionSentinel,
                selfDamageSentinel,
                trollPotionSentinel,

                betterSprintingSentinel,
                fiveZigSentinel,
                labyModSentinel,
                schematicaSentinel,
                vapeSentinel,
                worldDownloaderSentinel));

        // Add sentinel custom modules.
        ConfigUtils.loadKeys("Sentinel.Custom").stream().map(key -> new SentinelChannelModule("Custom." + key)).forEach(moduleList::add);

        moduleMap = new ModuleMap<>(moduleList);
        violationModuleMap = new ModuleMap<>(moduleMap.values().stream()
                                                      .filter(ViolationModule.class::isInstance)
                                                      .map(ViolationModule.class::cast)
                                                      .collect(Collectors.toList()));
        moduleMap.values().forEach(Module::enableModule);
    }

    public static void addExternalModule(final Module externalModule)
    {
        moduleMap.addModule(externalModule);
        if (externalModule instanceof ViolationModule) violationModuleMap.addModule((ViolationModule) externalModule);
    }

    /**
     * This creates the actual hook for the AAC API.
     */
    public static AACCustomFeatureProvider getCustomFeatureProvider()
    {
        return offlinePlayer -> {
            val uuid = offlinePlayer.getUniqueId();
            return violationModuleMap.values().stream()
                                     .filter(Module::isEnabled)
                                     .filter(module -> module.getAacInfo() != null)
                                     // Map the module and its AACScore to an AACCustomFeature.
                                     .map(module -> Pair.map(module, module.getAACScore(uuid),
                                                             (m, score) -> new AACCustomFeature(m.getConfigString(), m.getAacInfo(), score, m.getAACTooltip(uuid, score))))
                                     .collect(Collectors.toList());
        };
    }
}
