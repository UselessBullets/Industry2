package turniplabs.industry.blocks.entities.batbox

import sunsetsatellite.sunsetutils.util.Connection
import sunsetsatellite.sunsetutils.util.Direction

class TileEntityBatboxMV() : TileEntityBatboxBase() {

    init {
        this.setCapacity(32768)
        this.setTransfer(32)
        this.setMaxReceive(32)

        for (dir in Direction.values())
            this.setConnection(dir, Connection.OUTPUT)

        this.setConnection(Direction.Y_POS, Connection.INPUT)
    }

    override fun getInvName(): String {
        return "BatboxMV"
    }
}