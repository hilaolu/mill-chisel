circuit MooreFSM :
  module MooreFSM :
    input clock : Clock
    input reset : UInt<1>
    input io_in_signal : UInt<1>
    output io_out_found : UInt<1>

    node signal = bits(io_in_signal, 0, 0) @[moore.scala 24:35]
    reg current_state : UInt<3>, clock with :
      reset => (UInt<1>("h0"), current_state) @[moore.scala 26:30]
    node _T = mux(signal, UInt<3>("h2"), UInt<3>("h1")) @[moore.scala 31:24]
    node _T_1 = mux(signal, UInt<3>("h2"), UInt<3>("h1")) @[moore.scala 32:24]
    node _T_2 = mux(signal, UInt<3>("h2"), UInt<3>("h3")) @[moore.scala 33:24]
    node _T_3 = mux(signal, UInt<3>("h2"), UInt<3>("h4")) @[moore.scala 34:24]
    node _T_4 = mux(signal, UInt<3>("h5"), UInt<3>("h1")) @[moore.scala 35:24]
    node _T_5 = mux(signal, UInt<3>("h2"), UInt<3>("h6")) @[moore.scala 36:24]
    node _T_6 = mux(signal, UInt<3>("h2"), UInt<3>("h4")) @[moore.scala 37:24]
    node _next_state_T = eq(UInt<3>("h0"), current_state) @[Mux.scala 80:60]
    node _next_state_T_1 = mux(_next_state_T, _T, UInt<3>("h0")) @[Mux.scala 80:57]
    node _next_state_T_2 = eq(UInt<3>("h1"), current_state) @[Mux.scala 80:60]
    node _next_state_T_3 = mux(_next_state_T_2, _T_1, _next_state_T_1) @[Mux.scala 80:57]
    node _next_state_T_4 = eq(UInt<3>("h2"), current_state) @[Mux.scala 80:60]
    node _next_state_T_5 = mux(_next_state_T_4, _T_2, _next_state_T_3) @[Mux.scala 80:57]
    node _next_state_T_6 = eq(UInt<3>("h3"), current_state) @[Mux.scala 80:60]
    node _next_state_T_7 = mux(_next_state_T_6, _T_3, _next_state_T_5) @[Mux.scala 80:57]
    node _next_state_T_8 = eq(UInt<3>("h4"), current_state) @[Mux.scala 80:60]
    node _next_state_T_9 = mux(_next_state_T_8, _T_4, _next_state_T_7) @[Mux.scala 80:57]
    node _next_state_T_10 = eq(UInt<3>("h5"), current_state) @[Mux.scala 80:60]
    node _next_state_T_11 = mux(_next_state_T_10, _T_5, _next_state_T_9) @[Mux.scala 80:57]
    node _next_state_T_12 = eq(UInt<3>("h6"), current_state) @[Mux.scala 80:60]
    node _next_state_T_13 = mux(_next_state_T_12, _T_6, _next_state_T_11) @[Mux.scala 80:57]
    node _io_out_found_T = eq(current_state, UInt<3>("h6")) @[moore.scala 43:32]
    node next_state = _next_state_T_13 @[moore.scala 25:24 moore.scala 40:15]
    io_out_found <= _io_out_found_T @[moore.scala 43:17]
    current_state <= mux(reset, UInt<3>("h0"), next_state) @[moore.scala 26:30 moore.scala 26:30 moore.scala 28:18]
