package Calculator

import chisel3._
import chisel3.util._

class Multiplier extends MultiIOModule {

    val io = IO(
      new Protocol
    )

    io.out.result := io.in.op_1 * io.in.op_2
    //todo use booth

    io.out.end := true.B
    io.out.busy := false.B

}
