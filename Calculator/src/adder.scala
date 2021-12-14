package Calculator

import chisel3._
import chisel3.util._

class Adder extends MultiIOModule{

    val io = IO(
        new Bundle {
            val in=new Bundle{
                val op_1=Input(UInt(32.W))
                val op_2=Input(UInt(32.W))
                val start=Input(Bool())
            }

            val out=new Bundle{
                val result=Output(UInt(32.W))
                val end=Output(Bool())
                val busy=Output(Bool())
            }
        }
    )

    io.out.result:=io.in.op_1+io.in.op_2
    //todo use booth


    io.out.end:=true.B
    io.out.busy:=false.B

}