package Calculator

import chisel3._
import chisel3.util._

class Calculator extends MultiIOModule {

    val io = IO(new Bundle {
        val in = new Bundle {
            val start = Input(Bool())
            val op_1 = Input(UInt(8.W))
            val op_2 = Input(UInt(8.W))
            val opcode = Input(UInt(3.W))
        }

        val out = new Bundle {
            val en = Output(UInt(8.W))
            val cx = Output(UInt(8.W))
        }
    })

    val busy :: idle :: Nil = Enum(2)
    val state = RegInit(UInt(1.W), idle)

    val started = RegInit(Bool(), false.B)

    val opcode = io.in.opcode

    val result = RegInit(UInt(32.W), 0.U)
    //io.out.result := result

    val subtracter = Module(new Subtracter)
    val adder = Module(new Adder)
    val multiplier = Module(new Booth2)
    val divider = Module(new Divider2)

    val op_1 = Wire(UInt(32.W))
    val op_2 = Wire(UInt(32.W))

    op_2 := Mux(opcode === "b101".U, result, io.in.op_2)
    op_1 := Mux(opcode === "b101".U || started, result, io.in.op_1)

    subtracter.io.in.op_1 := op_1
    subtracter.io.in.op_2 := op_2

    divider.io.in.op_1 := op_1
    divider.io.in.op_2 := op_2

    multiplier.io.in.op_1 := op_1
    multiplier.io.in.op_2 := op_2

    adder.io.in.op_1 := op_1
    adder.io.in.op_2 := op_2

    multiplier.io.in.start := Mux(
      opcode === "b010".U || opcode === "b101".U,
      io.in.start && state === idle,
      false.B
    )
    divider.io.in.start := Mux(
      opcode === "b100".U || opcode === "b011".U,
      io.in.start && state === idle,
      false.B
    )

    adder.io.in.start := DontCare
    subtracter.io.in.start := DontCare

    val lut = Array(
      "b000".U -> adder.io.out.result,
      "b001".U -> subtracter.io.out.result,
      "b010".U -> multiplier.io.out.result,
      "b011".U -> divider.io.out.result,
      "b100".U -> divider.io.out.rest,
      "b101".U -> multiplier.io.out.result
    )

    val end_lut = Array(
      "b000".U -> adder.io.out.end,
      "b001".U -> subtracter.io.out.end,
      "b010".U -> multiplier.io.out.end,
      "b011".U -> divider.io.out.end,
      "b100".U -> divider.io.out.end,
      "b101".U -> multiplier.io.out.end
    )
    
    val next_state=Wire(UInt(1.W))
    val new_result = MuxLookup(opcode, 0.U, lut)

    val end = MuxLookup(opcode, false.B, end_lut)

    result := Mux(state===busy && next_state===idle, new_result, result)

    val driver = Module(new DisplayDriver)
    

    when(state === idle) {
        next_state := Mux(io.in.start, busy, idle)
    }.otherwise {
        next_state := Mux(end && ~io.in.start, idle, busy)
    }
    state:=next_state
    driver.io.in.hex_vec := result.asTypeOf(Vec(8, UInt(4.W)))

    started := Mux(state === busy, true.B, started)

    io.out.cx <> driver.io.out.cx
    io.out.en <> driver.io.out.index

}
