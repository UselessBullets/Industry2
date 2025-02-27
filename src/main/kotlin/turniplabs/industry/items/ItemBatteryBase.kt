package turniplabs.industry.items

import net.minecraft.core.item.ItemStack
import net.minecraft.core.net.command.TextFormatting
import sunsetsatellite.energyapi.EnergyAPI
import sunsetsatellite.energyapi.template.items.ItemBattery
import sunsetsatellite.sunsetutils.util.ICustomDescription
import turniplabs.halplibe.helper.TextureHelper
import turniplabs.industry.Industry2

open class ItemBatteryBase(
    i: Int,
    capacity: Int,
    provide: Int,
    receive: Int,
    fullTexture: String,
    midFullTexture: String,
    midTexture: String,
    midEmptyTexture: String,
    emptyTexture: String
) : ItemBattery(i), ICustomDescription {
    private var textureCoordinates: Array<IntArray?> = arrayOfNulls(5)

    init {
        baseCapacity = capacity
        baseProvide = provide
        baseReceive = receive
        val modID = Industry2.MOD_ID
        maxStackSize = 1

        textureCoordinates[0] = TextureHelper.getOrCreateItemTexture(modID, fullTexture)
        textureCoordinates[1] = TextureHelper.getOrCreateItemTexture(modID, midFullTexture)
        textureCoordinates[2] = TextureHelper.getOrCreateItemTexture(modID, midTexture)
        textureCoordinates[3] = TextureHelper.getOrCreateItemTexture(modID, midEmptyTexture)
        textureCoordinates[4] = TextureHelper.getOrCreateItemTexture(modID, emptyTexture)
    }

    override fun getIconIndex(itemstack: ItemStack?): Int {
        val mapped = EnergyAPI.map(
            (getEnergy(itemstack).toFloat() / getCapacity(itemstack)).toDouble(),
            0.0,
            1.0,
            0.0,
            4.0
        ).toInt()

        setIconCoord(textureCoordinates[mapped]!![0], textureCoordinates[mapped]!![1])
        return iconIndex
    }

    override fun getDescription(itemstack: ItemStack): String {
        val text = StringBuilder()

        return text.append("${TextFormatting.WHITE}Max Transfer: ${TextFormatting.LIGHT_GRAY}IN: $baseReceive${TextFormatting.WHITE} / ${TextFormatting.LIGHT_GRAY}OUT: $baseProvide\n")
            .append("${TextFormatting.WHITE}Energy: ${TextFormatting.LIGHT_GRAY}${getEnergy(itemstack)}${TextFormatting.WHITE} / ${baseCapacity}").toString()
    }
}