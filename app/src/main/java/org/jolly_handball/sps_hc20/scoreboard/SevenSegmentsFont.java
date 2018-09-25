package org.jolly_handball.sps_hc20.scoreboard;

class SevenSegmentsFont {

    private static byte[] FONT = new byte[]{
            //
            // the most significant bit, which controls the display's dot, is always
            // off because it controls the siren; the characters that should have it
            // on are: ! % . ?
            //
            //    .gfedcba
            0b00000000, //  <empty space>
            0b00000110, //  !
            0b00100010, //  "
            0b01111110, //  #
            0b01101101, //  $
            0b01010010, //  %
            0b01000110, //  &
            0b00100000, //  '
            0b00101001, //  (
            0b00001011, //  )
            0b00100001, //  *
            0b01110000, //  +
            0b00010000, //  ,
            0b01000000, //  -
            0b00000000, //  .
            0b01010010, //  /
            0b00111111, //  0
            0b00000110, //  1
            0b01011011, //  2
            0b01001111, //  3
            0b01100110, //  4
            0b01101101, //  5
            0b01111101, //  6
            0b00000111, //  7
            0b01111111, //  8
            0b01101111, //  9
            0b00001001, //  :
            0b00001101, //  ;
            0b01100001, //  <
            0b01001000, //  =
            0b01000011, //  >
            0b01010011, //  ?
            0b01011111, //  @
            0b01110111, //  A
            0b01111111, //  B
            0b00111001, //  C
            0b00011111, //  D
            0b01111001, //  E
            0b01110001, //  F
            0b00111101, //  G
            0b01110110, //  H
            0b00110000, //  I
            0b00011110, //  J
            0b01111010, //  K
            0b00111000, //  L
            0b00010101, //  M
            0b00110111, //  N
            0b00111111, //  O
            0b01110011, //  P
            0b01101011, //  Q
            0b00110011, //  R
            0b01101101, //  S
            0b00000111, //  T
            0b00111110, //  U
            0b01110010, //  V
            0b01111110, //  W
            0b01100100, //  X
            0b01101110, //  Y
            0b01011011, //  Z
            0b00111001, //  [
            0b01100100, //  <backslash>
            0b00001111, //  ]
            0b00100011, //  ^
            0b00001000, //  _
            0b00000010, //  `
            0b01011111, //  a
            0b01111100, //  b
            0b01011000, //  c
            0b01011110, //  d
            0b01111011, //  e
            0b01110001, //  f
            0b01101111, //  g
            0b01110100, //  h
            0b00010000, //  i
            0b00001100, //  j
            0b01110101, //  k
            0b00110000, //  l
            0b00010100, //  m
            0b01010100, //  n
            0b01011100, //  o
            0b01110011, //  p
            0b01100111, //  q
            0b01010000, //  r
            0b01101101, //  s
            0b01111000, //  t
            0b00011100, //  u
            0b01100010, //  v
            0b00101010, //  w
            0b01010010, //  x
            0b01101110, //  y
            0b01011011, //  z
            0b01000110, //  {
            0b00110000, //  |
            0b01110000, //  }
            0b00000001, //  ~
    };

    private static byte BLANK = FONT[0];

    byte encode(Character character) {
        if (character < 31 || character > 128)
            return BLANK;
        return FONT[character - 32];
    }

    byte[] encode(String text) {
        byte[] response = new byte[text.length()];

        for (int i = 0; i < text.length(); i++)
            response[i] = encode(text.charAt(i));

        return response;
    }
}
