;buildInfoPackage: chisel3, version: 3.4.3, scalaVersion: 2.12.12, sbtVersion: 1.3.10
circuit MooreFSM : 
  module MooreFSM : 
    input clock : Clock
    input reset : UInt<1>
    output io : {in : {flip signal : UInt<1>}, out : {found : UInt<1>}}
    
    node signal = bits(io.in.signal, 0, 0) @[moore.scala 24:35]
    wire next_state : UInt<3> @[moore.scala 25:24]
    reg current_state : UInt<3>, clock with : (reset => (reset, UInt<3>("h00"))) @[moore.scala 26:30]
    current_state <= next_state @[moore.scala 28:18]
    node _T = mux(signal, UInt<3>("h02"), UInt<3>("h01")) @[moore.scala 31:24]
    node _T_1 = mux(signal, UInt<3>("h02"), UInt<3>("h01")) @[moore.scala 32:24]
    node _T_2 = mux(signal, UInt<3>("h02"), UInt<3>("h03")) @[moore.scala 33:24]
    node _T_3 = mux(signal, UInt<3>("h02"), UInt<3>("h04")) @[moore.scala 34:24]
    node _T_4 = mux(signal, UInt<3>("h05"), UInt<3>("h01")) @[moore.scala 35:24]
    node _T_5 = mux(signal, UInt<3>("h02"), UInt<3>("h06")) @[moore.scala 36:24]
    node _T_6 = mux(signal, UInt<3>("h02"), UInt<3>("h04")) @[moore.scala 37:24]
    node _next_state_T = eq(UInt<3>("h00"), current_state) @[Mux.scala 80:60]
    node _next_state_T_1 = mux(_next_state_T, _T, UInt<3>("h00")) @[Mux.scala 80:57]
    node _next_state_T_2 = eq(UInt<3>("h01"), current_state) @[Mux.scala 80:60]
    node _next_state_T_3 = mux(_next_state_T_2, _T_1, _next_state_T_1) @[Mux.scala 80:57]
    node _next_state_T_4 = eq(UInt<3>("h02"), current_state) @[Mux.scala 80:60]
    node _next_state_T_5 = mux(_next_state_T_4, _T_2, _next_state_T_3) @[Mux.scala 80:57]
    node _next_state_T_6 = eq(UInt<3>("h03"), current_state) @[Mux.scala 80:60]
    node _next_state_T_7 = mux(_next_state_T_6, _T_3, _next_state_T_5) @[Mux.scala 80:57]
    node _next_state_T_8 = eq(UInt<3>("h04"), current_state) @[Mux.scala 80:60]
    node _next_state_T_9 = mux(_next_state_T_8, _T_4, _next_state_T_7) @[Mux.scala 80:57]
    node _next_state_T_10 = eq(UInt<3>("h05"), current_state) @[Mux.scala 80:60]
    node _next_state_T_11 = mux(_next_state_T_10, _T_5, _next_state_T_9) @[Mux.scala 80:57]
    node _next_state_T_12 = eq(UInt<3>("h06"), current_state) @[Mux.scala 80:60]
    node _next_state_T_13 = mux(_next_state_T_12, _T_6, _next_state_T_11) @[Mux.scala 80:57]
    next_state <= _next_state_T_13 @[moore.scala 40:15]
    node _io_out_found_T = eq(current_state, UInt<3>("h06")) @[moore.scala 43:32]
    io.out.found <= _io_out_found_T @[moore.scala 43:17]
    
