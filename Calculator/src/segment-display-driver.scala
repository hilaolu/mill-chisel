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
      //  ?ABC_DEFG
      0.U -> "b0111_1110".U,
      1.U -> "b0011_0000".U,
      2.U -> "b0110_1101".U,
      3.U -> "b0111_1001".U,
      4.U -> "b0011_0011".U,
      5.U -> "b0101_1011".U,
      6.U -> "b0101_1111".U,
      7.U -> "b0111_0000".U,
      8.U -> "b0111_1111".U,
      9.U -> "b0111_1011".U,
      10.U -> "b0111_0111".U,
      11.U -> "b0001_1111".U,
      12.U -> "b0100_1110".U,
      13.U -> "b0111_1101".U,
      14.U -> "b0100_1111".U,
      15.U -> "b0100_0111".U
    )

    val counter = RegInit(UInt(32.W), 0.U)

    counter := counter + 1.U

    val index = counter(25, 23)

    io.out.cx := MuxLookup(io.in.hex_vec(index), 0.U, lut)

    io.out.index := ~(1.U << index)

}
