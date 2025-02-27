package turniplabs.industry.blocks.entities.solar

import net.minecraft.core.block.Block
import sunsetsatellite.energyapi.impl.ItemEnergyContainer
import sunsetsatellite.energyapi.template.tiles.TileEntityBatteryBox
import kotlin.math.min

open class TileEntitySolarBase(private val voltage: Int): TileEntityBatteryBox() {
    var generatedEnergy = 0

    override fun updateEntity() {
        super.updateEntity()

        if (getStackInSlot(0) != null && getStackInSlot(0)?.item is ItemEnergyContainer) {
            provide(getStackInSlot(0), getMaxProvide(), false)
            onInventoryChanged()
        }

        if (getStackInSlot(1) != null && getStackInSlot(1)?.item is ItemEnergyContainer) {
            receive(getStackInSlot(1), getMaxReceive(), false)
            onInventoryChanged()
        }

        if (energy < capacity && isFacingSky()) {
            generatedEnergy = 2 * voltage

            generatedEnergy -= worldObj.skyDarken * voltage

            if (worldObj.getBlockTemperature(xCoord, zCoord) > 0.85f && worldObj.getBlockHumidity(xCoord, zCoord) < 0.30f)
                generatedEnergy += 2

            if (worldObj.getBlockTemperature(xCoord, zCoord) < 0.40f && worldObj.getBlockHumidity(xCoord, zCoord) < 0.30f)
                generatedEnergy -= 2

            if (generatedEnergy > 0)
                energy = min(energy + generatedEnergy, capacity)
        }

        if (!isFacingSky())
            generatedEnergy = 0
    }

    private fun isFacingSky(): Boolean {
        for (heightCoord in yCoord + 1..255) {
            val block = Block.getBlock(worldObj.getBlockId(xCoord, heightCoord, zCoord))
            if (block != null && block.isOpaqueCube) return false
        }
        return true
    }
}