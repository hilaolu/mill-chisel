package Calculator

import chisel3._
import chisel3.util._

class DisplayDriver extends MultiIOModule {

    val io = IO(new Bundle {
        val in = new Bundle {
            val hex_vec = Input(Vec(8, UInt(4.W)))
        }

        val out = new Bundle {
            val index = Output(UInt(8.W))
            val cx = Output(UInt(8.W))
        }

    })

    val lut = Array(
      //  ABCD_EFGH
      0.U -> "b1111_1100".U,
      1.U -> "b0110_0000".U,
      2.U -> "b1101_1010".U,
      3.U -> "b1111_0010".U,
      4.U -> "b0110_0110".U,
      5.U -> "b1011_0110".U,
      6.U -> "b1011_1110".U,
      7.U -> "b1110_0000".U,
      8.U -> "b1111_1110".U,
      9.U -> "b1111_0110".U,
      10.U -> "b1110_1110".U,
      11.U -> "b0011_1110".U,
      12.U -> "b1001_1100".U,
      13.U -> "b0111_1010".U,
      14.U -> "b1001_1110".U,
      15.U -> "b1000_1110".U
    )

    val counter = RegInit(UInt(32.W), 0.U)

    counter := counter + 1.U

    val index = counter(18, 16)

    io.out.cx := ~MuxLookup(io.in.hex_vec(index), 0.U, lut)

    io.out.index := ~(1.U << index)

}
