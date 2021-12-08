package FSM

import chisel3._
import chisel3.util._

class MooreFSM extends MultiIOModule {

  //checks seq 10011
  //please pay attention to the bitorder
    val io = IO(
        new Bundle {
            val in=new Bundle{
                val signal=Input(UInt(1.W))
            }
            
            val out=new Bundle{
                val found=Output(UInt(1.W))
            }
        }
    )

    val sinit::s0::s1::s10::s100::s1001::s10011::Nil=util.Enum(7)

    val signal=io.in.signal.asBool()
    val next_state=Wire(UInt(3.W))
    val current_state=RegInit(sinit)

    current_state:=next_state
    
    val state_matrix = Array(
        sinit    -> Mux(signal,s1,s0),
        s0       -> Mux(signal,s1,s0),
        s1       -> Mux(signal,s1,s10),
        s10      -> Mux(signal,s1,s100),
        s100     -> Mux(signal,s1001,s0),
        s1001    -> Mux(signal,s10011,s10),
        s10011   -> Mux(signal,s1,s10),
    )

    next_state:=MuxLookup(current_state,sinit,state_matrix)


    io.out.found:=current_state===s10011

}

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
}