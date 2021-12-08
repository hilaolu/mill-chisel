package FSM

import chisel3._
import chisel3.util._

class MealyFSM extends MultiIOModule {

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

    next_state:=sinit 
    //default status
    //chisel will throw an error without this
    //since seven status don't full cover 3 bits

    switch(current_state){
        is(sinit){
            next_state:=Mux(signal,s1,s0)
        }
        is(s0){
            next_state:=Mux(signal,s1,s0)
        }
        is(s1){
            next_state:=Mux(signal,s1,s10)
        }
        is(s10){
            next_state:=Mux(signal,s1,s100)
        }
        is(s100){
            next_state:=Mux(signal,s1001,s0)
        }
        is(s1001){
            next_state:=Mux(signal,s10011,s10)
        }
        is(s10011){
            next_state:=Mux(signal,s1,s10)
        }
    }

    io.out.found:=current_state===s1001&&signal

}
