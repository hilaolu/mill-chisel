package FSM

import chisel3._
import chisel3.util._
/***
 * This module helps to print Verilog of the generated module.
 * To run this main function with mill from the terminal:
 * {{{
 * mill MooreFSM
 * }}}
 */ 
object GiveMeVerilog extends App {
    import chisel3.stage._
    val c = new ChiselStage
    println(c.emitVerilog(new MooreFSM,Array("--target-dir", "build/")))
    println(c.emitVerilog(new MealyFSM,Array("--target-dir", "build/")))
}