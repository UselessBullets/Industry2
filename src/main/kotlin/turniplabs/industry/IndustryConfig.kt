package turniplabs.industry

import turniplabs.halplibe.util.ConfigUpdater
import turniplabs.halplibe.util.TomlConfigHandler
import turniplabs.halplibe.util.toml.Toml


object IndustryConfig {
    private val updater = ConfigUpdater.fromProperties()
    private val properties = Toml("Industry2's TOML Config")
    var cfg: TomlConfigHandler

    private var blockID = 1000
    private fun nextBlockID(): Int {
        return blockID++
    }

    private var itemID = 17000
    private fun nextItemID(): Int {
        return itemID++
    }

    init {
        properties.addCategory("Energy Values")
            .addEntry("lowVoltage", 16)
            .addEntry("mediumVoltage", 32)
            .addEntry("highVoltage", 512)
            .addEntry("superHighVoltage", 1024)

        properties.addCategory("Block IDs")
            .addEntry("oreCopperStone", nextBlockID())
            .addEntry("oreCopperBasalt", nextBlockID())
            .addEntry("oreCopperLimestone", nextBlockID())
            .addEntry("oreCopperGranite", nextBlockID())
            .addEntry("oreTinStone", nextBlockID())
            .addEntry("oreTinBasalt", nextBlockID())
            .addEntry("oreTinLimestone", nextBlockID())
            .addEntry("oreTinGranite", nextBlockID())
            .addEntry("oreUraniumStone", nextBlockID())
            .addEntry("oreUraniumBasalt", nextBlockID())
            .addEntry("oreUraniumLimestone", nextBlockID())
            .addEntry("oreUraniumGranite", nextBlockID())
            .addEntry("blockCopper", nextBlockID())
            .addEntry("blockTin", nextBlockID())
            .addEntry("blockBronze", nextBlockID())
            .addEntry("blockUranium", nextBlockID())
            .addEntry("blockCopperCable", nextBlockID())
            .addEntry("blockTinCable", nextBlockID())
            .addEntry("blockGoldCable", nextBlockID())
            .addEntry("blockSteelCable", nextBlockID())
            .addEntry("blockInsulatedCopperCable", nextBlockID())
            .addEntry("blockInsulatedTinCable", nextBlockID())
            .addEntry("blockInsulatedGoldCable", nextBlockID())
            .addEntry("blockInsulatedSteelCable", nextBlockID())
            .addEntry("machineCasing", nextBlockID())
            .addEntry("machineCasingAdvanced", nextBlockID())
            .addEntry("generator", nextBlockID())
            .addEntry("generatorWatermill", nextBlockID())
            .addEntry("generatorWindmill", nextBlockID())
            .addEntry("generatorGeothermal", nextBlockID())
            .addEntry("generatorSolar", nextBlockID())
            .addEntry("generatorSolarArrayLV", nextBlockID())
            .addEntry("generatorSolarArrayMV", nextBlockID())
            .addEntry("generatorSolarArrayHV", nextBlockID())
            .addEntry("generatorSolarArraySHV", nextBlockID())
            .addEntry("batboxLV", nextBlockID())
            .addEntry("batboxMV", nextBlockID())
            .addEntry("batboxHV", nextBlockID())
            .addEntry("batboxSHV", nextBlockID())
            .addEntry("transformerMVtoLV", nextBlockID())
            .addEntry("transformerHVtoMV", nextBlockID())
            .addEntry("transformerSHVtoMV", nextBlockID())
            .addEntry("electricFurnace", nextBlockID())
            .addEntry("macerator", nextBlockID())
            .addEntry("compressor", nextBlockID())
            .addEntry("wiremill", nextBlockID())
            .addEntry("extractor", nextBlockID())
            .addEntry("recycler", nextBlockID())
            .addEntry("cannery", nextBlockID())
            .addEntry("inductionFurnace", nextBlockID())
            .addEntry("rotaryMacerator", nextBlockID())
            .addEntry("singularityCompressor", nextBlockID())
            .addEntry("laserCutter", nextBlockID())
            .addEntry("CentrifugeExtractor", nextBlockID())
            .addEntry("hardenedCoal", nextBlockID())
            .addEntry("rubberLeaves", nextBlockID())
            .addEntry("rubberLog", nextBlockID())
            .addEntry("rubberSapling", nextBlockID())

        properties.addCategory("Item IDs")
            .addEntry("rawCopper", nextItemID())
            .addEntry("rawTin", nextItemID())
            .addEntry("rawUranium", nextItemID())
            .addEntry("dustCopper", nextItemID())
            .addEntry("dustTin", nextItemID())
            .addEntry("dustBronze", nextItemID())
            .addEntry("dustIron", nextItemID())
            .addEntry("dustGold", nextItemID())
            .addEntry("dustCoal", nextItemID())
            .addEntry("ingotCopper", nextItemID())
            .addEntry("ingotTin", nextItemID())
            .addEntry("ingotBronze", nextItemID())
            .addEntry("ingotUranium", nextItemID())
            .addEntry("plateCopper", nextItemID())
            .addEntry("plateTin", nextItemID())
            .addEntry("plateBronze", nextItemID())
            .addEntry("plateIron", nextItemID())
            .addEntry("plateGold", nextItemID())
            .addEntry("plateSteel", nextItemID())
            .addEntry("itemCopperCable", nextItemID())
            .addEntry("itemTinCable", nextItemID())
            .addEntry("itemGoldCable", nextItemID())
            .addEntry("itemSteelCable", nextItemID())
            .addEntry("itemInsulatedCopperCable", nextItemID())
            .addEntry("itemInsulatedTinCable", nextItemID())
            .addEntry("itemInsulatedGoldCable", nextItemID())
            .addEntry("itemInsulatedSteelCable", nextItemID())
            .addEntry("toolHammer", nextItemID())
            .addEntry("toolCutter", nextItemID())
            .addEntry("toolWrench", nextItemID())
            .addEntry("toolTreetap", nextItemID())
            .addEntry("batteryRedstone", nextItemID())
            .addEntry("batteryAdvanced", nextItemID())
            .addEntry("batteryCrystal", nextItemID())
            .addEntry("batteryLapis", nextItemID())
            .addEntry("cellEmpty", nextItemID())
            .addEntry("cellWater", nextItemID())
            .addEntry("cellLava", nextItemID())
            .addEntry("cellUranium", nextItemID())
            .addEntry("cellCoolant", nextItemID())
            .addEntry("canEmpty", nextItemID())
            .addEntry("canFood", nextItemID())
            .addEntry("resin", nextItemID())
            .addEntry("rubber", nextItemID())
            .addEntry("circuitBasic", nextItemID())
            .addEntry("circuitAdvanced", nextItemID())
            .addEntry("scrap", nextItemID())

        cfg = TomlConfigHandler(updater, Industry2.MOD_ID, properties)
    }
}