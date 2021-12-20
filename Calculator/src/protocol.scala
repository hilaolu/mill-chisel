package Calculator

import chisel3._
import chisel3.util._

class Protocol extends Bundle {
    val in = new Bundle {
        val op_1 = Input(UInt(32.W))
        val op_2 = Input(UInt(32.W))
        val start = Input(Bool())
    }

    val out = new Bundle {
        val result = Output(UInt(32.W))
        val end = Output(Bool())
        val busy = Output(Bool())
    }
}

class DividerProtocol extends Protocol {

    override val out = new Bundle {
        val result = Output(UInt(32.W))
        val end = Output(Bool())
        val busy = Output(Bool())
        val rest = Output(UInt(32.W))
    }
}
