

public final class KingMove {
	
	
static final int KingMove[][] = new int[64][];

static {
	
KingMove[0] = new int[3];
KingMove[0][0] = 8;
KingMove[0][1] = 1;
KingMove[0][2] = 9;
KingMove[8] = new int[5];
KingMove[8][0] = 0;
KingMove[8][1] = 16;
KingMove[8][2] = 1;
KingMove[8][3] = 9;
KingMove[8][4] = 17;
KingMove[16] = new int[5];
KingMove[16][0] = 8;
KingMove[16][1] = 24;
KingMove[16][2] = 9;
KingMove[16][3] = 17;
KingMove[16][4] = 25;
KingMove[24] = new int[5];
KingMove[24][0] = 16;
KingMove[24][1] = 32;
KingMove[24][2] = 17;
KingMove[24][3] = 25;
KingMove[24][4] = 33;
KingMove[32] = new int[5];
KingMove[32][0] = 24;
KingMove[32][1] = 40;
KingMove[32][2] = 25;
KingMove[32][3] = 33;
KingMove[32][4] = 41;
KingMove[40] = new int[5];
KingMove[40][0] = 32;
KingMove[40][1] = 48;
KingMove[40][2] = 33;
KingMove[40][3] = 41;
KingMove[40][4] = 49;
KingMove[48] = new int[5];
KingMove[48][0] = 40;
KingMove[48][1] = 56;
KingMove[48][2] = 41;
KingMove[48][3] = 49;
KingMove[48][4] = 57;
KingMove[56] = new int[3];
KingMove[56][0] = 48;
KingMove[56][1] = 49;
KingMove[56][2] = 57;
KingMove[1] = new int[5];
KingMove[1][0] = 0;
KingMove[1][1] = 8;
KingMove[1][2] = 9;
KingMove[1][3] = 2;
KingMove[1][4] = 10;
KingMove[9] = new int[8];
KingMove[9][0] = 0;
KingMove[9][1] = 8;
KingMove[9][2] = 16;
KingMove[9][3] = 1;
KingMove[9][4] = 17;
KingMove[9][5] = 2;
KingMove[9][6] = 10;
KingMove[9][7] = 18;
KingMove[17] = new int[8];
KingMove[17][0] = 8;
KingMove[17][1] = 16;
KingMove[17][2] = 24;
KingMove[17][3] = 9;
KingMove[17][4] = 25;
KingMove[17][5] = 10;
KingMove[17][6] = 18;
KingMove[17][7] = 26;
KingMove[25] = new int[8];
KingMove[25][0] = 16;
KingMove[25][1] = 24;
KingMove[25][2] = 32;
KingMove[25][3] = 17;
KingMove[25][4] = 33;
KingMove[25][5] = 18;
KingMove[25][6] = 26;
KingMove[25][7] = 34;
KingMove[33] = new int[8];
KingMove[33][0] = 24;
KingMove[33][1] = 32;
KingMove[33][2] = 40;
KingMove[33][3] = 25;
KingMove[33][4] = 41;
KingMove[33][5] = 26;
KingMove[33][6] = 34;
KingMove[33][7] = 42;
KingMove[41] = new int[8];
KingMove[41][0] = 32;
KingMove[41][1] = 40;
KingMove[41][2] = 48;
KingMove[41][3] = 33;
KingMove[41][4] = 49;
KingMove[41][5] = 34;
KingMove[41][6] = 42;
KingMove[41][7] = 50;
KingMove[49] = new int[8];
KingMove[49][0] = 40;
KingMove[49][1] = 48;
KingMove[49][2] = 56;
KingMove[49][3] = 41;
KingMove[49][4] = 57;
KingMove[49][5] = 42;
KingMove[49][6] = 50;
KingMove[49][7] = 58;
KingMove[57] = new int[5];
KingMove[57][0] = 48;
KingMove[57][1] = 56;
KingMove[57][2] = 49;
KingMove[57][3] = 50;
KingMove[57][4] = 58;
KingMove[2] = new int[5];
KingMove[2][0] = 1;
KingMove[2][1] = 9;
KingMove[2][2] = 10;
KingMove[2][3] = 3;
KingMove[2][4] = 11;
KingMove[10] = new int[8];
KingMove[10][0] = 1;
KingMove[10][1] = 9;
KingMove[10][2] = 17;
KingMove[10][3] = 2;
KingMove[10][4] = 18;
KingMove[10][5] = 3;
KingMove[10][6] = 11;
KingMove[10][7] = 19;
KingMove[18] = new int[8];
KingMove[18][0] = 9;
KingMove[18][1] = 17;
KingMove[18][2] = 25;
KingMove[18][3] = 10;
KingMove[18][4] = 26;
KingMove[18][5] = 11;
KingMove[18][6] = 19;
KingMove[18][7] = 27;
KingMove[26] = new int[8];
KingMove[26][0] = 17;
KingMove[26][1] = 25;
KingMove[26][2] = 33;
KingMove[26][3] = 18;
KingMove[26][4] = 34;
KingMove[26][5] = 19;
KingMove[26][6] = 27;
KingMove[26][7] = 35;
KingMove[34] = new int[8];
KingMove[34][0] = 25;
KingMove[34][1] = 33;
KingMove[34][2] = 41;
KingMove[34][3] = 26;
KingMove[34][4] = 42;
KingMove[34][5] = 27;
KingMove[34][6] = 35;
KingMove[34][7] = 43;
KingMove[42] = new int[8];
KingMove[42][0] = 33;
KingMove[42][1] = 41;
KingMove[42][2] = 49;
KingMove[42][3] = 34;
KingMove[42][4] = 50;
KingMove[42][5] = 35;
KingMove[42][6] = 43;
KingMove[42][7] = 51;
KingMove[50] = new int[8];
KingMove[50][0] = 41;
KingMove[50][1] = 49;
KingMove[50][2] = 57;
KingMove[50][3] = 42;
KingMove[50][4] = 58;
KingMove[50][5] = 43;
KingMove[50][6] = 51;
KingMove[50][7] = 59;
KingMove[58] = new int[5];
KingMove[58][0] = 49;
KingMove[58][1] = 57;
KingMove[58][2] = 50;
KingMove[58][3] = 51;
KingMove[58][4] = 59;
KingMove[3] = new int[5];
KingMove[3][0] = 2;
KingMove[3][1] = 10;
KingMove[3][2] = 11;
KingMove[3][3] = 4;
KingMove[3][4] = 12;
KingMove[11] = new int[8];
KingMove[11][0] = 2;
KingMove[11][1] = 10;
KingMove[11][2] = 18;
KingMove[11][3] = 3;
KingMove[11][4] = 19;
KingMove[11][5] = 4;
KingMove[11][6] = 12;
KingMove[11][7] = 20;
KingMove[19] = new int[8];
KingMove[19][0] = 10;
KingMove[19][1] = 18;
KingMove[19][2] = 26;
KingMove[19][3] = 11;
KingMove[19][4] = 27;
KingMove[19][5] = 12;
KingMove[19][6] = 20;
KingMove[19][7] = 28;
KingMove[27] = new int[8];
KingMove[27][0] = 18;
KingMove[27][1] = 26;
KingMove[27][2] = 34;
KingMove[27][3] = 19;
KingMove[27][4] = 35;
KingMove[27][5] = 20;
KingMove[27][6] = 28;
KingMove[27][7] = 36;
KingMove[35] = new int[8];
KingMove[35][0] = 26;
KingMove[35][1] = 34;
KingMove[35][2] = 42;
KingMove[35][3] = 27;
KingMove[35][4] = 43;
KingMove[35][5] = 28;
KingMove[35][6] = 36;
KingMove[35][7] = 44;
KingMove[43] = new int[8];
KingMove[43][0] = 34;
KingMove[43][1] = 42;
KingMove[43][2] = 50;
KingMove[43][3] = 35;
KingMove[43][4] = 51;
KingMove[43][5] = 36;
KingMove[43][6] = 44;
KingMove[43][7] = 52;
KingMove[51] = new int[8];
KingMove[51][0] = 42;
KingMove[51][1] = 50;
KingMove[51][2] = 58;
KingMove[51][3] = 43;
KingMove[51][4] = 59;
KingMove[51][5] = 44;
KingMove[51][6] = 52;
KingMove[51][7] = 60;
KingMove[59] = new int[5];
KingMove[59][0] = 50;
KingMove[59][1] = 58;
KingMove[59][2] = 51;
KingMove[59][3] = 52;
KingMove[59][4] = 60;
KingMove[4] = new int[5];
KingMove[4][0] = 3;
KingMove[4][1] = 11;
KingMove[4][2] = 12;
KingMove[4][3] = 5;
KingMove[4][4] = 13;
KingMove[12] = new int[8];
KingMove[12][0] = 3;
KingMove[12][1] = 11;
KingMove[12][2] = 19;
KingMove[12][3] = 4;
KingMove[12][4] = 20;
KingMove[12][5] = 5;
KingMove[12][6] = 13;
KingMove[12][7] = 21;
KingMove[20] = new int[8];
KingMove[20][0] = 11;
KingMove[20][1] = 19;
KingMove[20][2] = 27;
KingMove[20][3] = 12;
KingMove[20][4] = 28;
KingMove[20][5] = 13;
KingMove[20][6] = 21;
KingMove[20][7] = 29;
KingMove[28] = new int[8];
KingMove[28][0] = 19;
KingMove[28][1] = 27;
KingMove[28][2] = 35;
KingMove[28][3] = 20;
KingMove[28][4] = 36;
KingMove[28][5] = 21;
KingMove[28][6] = 29;
KingMove[28][7] = 37;
KingMove[36] = new int[8];
KingMove[36][0] = 27;
KingMove[36][1] = 35;
KingMove[36][2] = 43;
KingMove[36][3] = 28;
KingMove[36][4] = 44;
KingMove[36][5] = 29;
KingMove[36][6] = 37;
KingMove[36][7] = 45;
KingMove[44] = new int[8];
KingMove[44][0] = 35;
KingMove[44][1] = 43;
KingMove[44][2] = 51;
KingMove[44][3] = 36;
KingMove[44][4] = 52;
KingMove[44][5] = 37;
KingMove[44][6] = 45;
KingMove[44][7] = 53;
KingMove[52] = new int[8];
KingMove[52][0] = 43;
KingMove[52][1] = 51;
KingMove[52][2] = 59;
KingMove[52][3] = 44;
KingMove[52][4] = 60;
KingMove[52][5] = 45;
KingMove[52][6] = 53;
KingMove[52][7] = 61;
KingMove[60] = new int[5];
KingMove[60][0] = 51;
KingMove[60][1] = 59;
KingMove[60][2] = 52;
KingMove[60][3] = 53;
KingMove[60][4] = 61;
KingMove[5] = new int[5];
KingMove[5][0] = 4;
KingMove[5][1] = 12;
KingMove[5][2] = 13;
KingMove[5][3] = 6;
KingMove[5][4] = 14;
KingMove[13] = new int[8];
KingMove[13][0] = 4;
KingMove[13][1] = 12;
KingMove[13][2] = 20;
KingMove[13][3] = 5;
KingMove[13][4] = 21;
KingMove[13][5] = 6;
KingMove[13][6] = 14;
KingMove[13][7] = 22;
KingMove[21] = new int[8];
KingMove[21][0] = 12;
KingMove[21][1] = 20;
KingMove[21][2] = 28;
KingMove[21][3] = 13;
KingMove[21][4] = 29;
KingMove[21][5] = 14;
KingMove[21][6] = 22;
KingMove[21][7] = 30;
KingMove[29] = new int[8];
KingMove[29][0] = 20;
KingMove[29][1] = 28;
KingMove[29][2] = 36;
KingMove[29][3] = 21;
KingMove[29][4] = 37;
KingMove[29][5] = 22;
KingMove[29][6] = 30;
KingMove[29][7] = 38;
KingMove[37] = new int[8];
KingMove[37][0] = 28;
KingMove[37][1] = 36;
KingMove[37][2] = 44;
KingMove[37][3] = 29;
KingMove[37][4] = 45;
KingMove[37][5] = 30;
KingMove[37][6] = 38;
KingMove[37][7] = 46;
KingMove[45] = new int[8];
KingMove[45][0] = 36;
KingMove[45][1] = 44;
KingMove[45][2] = 52;
KingMove[45][3] = 37;
KingMove[45][4] = 53;
KingMove[45][5] = 38;
KingMove[45][6] = 46;
KingMove[45][7] = 54;
KingMove[53] = new int[8];
KingMove[53][0] = 44;
KingMove[53][1] = 52;
KingMove[53][2] = 60;
KingMove[53][3] = 45;
KingMove[53][4] = 61;
KingMove[53][5] = 46;
KingMove[53][6] = 54;
KingMove[53][7] = 62;
KingMove[61] = new int[5];
KingMove[61][0] = 52;
KingMove[61][1] = 60;
KingMove[61][2] = 53;
KingMove[61][3] = 54;
KingMove[61][4] = 62;
KingMove[6] = new int[5];
KingMove[6][0] = 5;
KingMove[6][1] = 13;
KingMove[6][2] = 14;
KingMove[6][3] = 7;
KingMove[6][4] = 15;
KingMove[14] = new int[8];
KingMove[14][0] = 5;
KingMove[14][1] = 13;
KingMove[14][2] = 21;
KingMove[14][3] = 6;
KingMove[14][4] = 22;
KingMove[14][5] = 7;
KingMove[14][6] = 15;
KingMove[14][7] = 23;
KingMove[22] = new int[8];
KingMove[22][0] = 13;
KingMove[22][1] = 21;
KingMove[22][2] = 29;
KingMove[22][3] = 14;
KingMove[22][4] = 30;
KingMove[22][5] = 15;
KingMove[22][6] = 23;
KingMove[22][7] = 31;
KingMove[30] = new int[8];
KingMove[30][0] = 21;
KingMove[30][1] = 29;
KingMove[30][2] = 37;
KingMove[30][3] = 22;
KingMove[30][4] = 38;
KingMove[30][5] = 23;
KingMove[30][6] = 31;
KingMove[30][7] = 39;
KingMove[38] = new int[8];
KingMove[38][0] = 29;
KingMove[38][1] = 37;
KingMove[38][2] = 45;
KingMove[38][3] = 30;
KingMove[38][4] = 46;
KingMove[38][5] = 31;
KingMove[38][6] = 39;
KingMove[38][7] = 47;
KingMove[46] = new int[8];
KingMove[46][0] = 37;
KingMove[46][1] = 45;
KingMove[46][2] = 53;
KingMove[46][3] = 38;
KingMove[46][4] = 54;
KingMove[46][5] = 39;
KingMove[46][6] = 47;
KingMove[46][7] = 55;
KingMove[54] = new int[8];
KingMove[54][0] = 45;
KingMove[54][1] = 53;
KingMove[54][2] = 61;
KingMove[54][3] = 46;
KingMove[54][4] = 62;
KingMove[54][5] = 47;
KingMove[54][6] = 55;
KingMove[54][7] = 63;
KingMove[62] = new int[5];
KingMove[62][0] = 53;
KingMove[62][1] = 61;
KingMove[62][2] = 54;
KingMove[62][3] = 55;
KingMove[62][4] = 63;
KingMove[7] = new int[3];
KingMove[7][0] = 6;
KingMove[7][1] = 14;
KingMove[7][2] = 15;
KingMove[15] = new int[5];
KingMove[15][0] = 6;
KingMove[15][1] = 14;
KingMove[15][2] = 22;
KingMove[15][3] = 7;
KingMove[15][4] = 23;
KingMove[23] = new int[5];
KingMove[23][0] = 14;
KingMove[23][1] = 22;
KingMove[23][2] = 30;
KingMove[23][3] = 15;
KingMove[23][4] = 31;
KingMove[31] = new int[5];
KingMove[31][0] = 22;
KingMove[31][1] = 30;
KingMove[31][2] = 38;
KingMove[31][3] = 23;
KingMove[31][4] = 39;
KingMove[39] = new int[5];
KingMove[39][0] = 30;
KingMove[39][1] = 38;
KingMove[39][2] = 46;
KingMove[39][3] = 31;
KingMove[39][4] = 47;
KingMove[47] = new int[5];
KingMove[47][0] = 38;
KingMove[47][1] = 46;
KingMove[47][2] = 54;
KingMove[47][3] = 39;
KingMove[47][4] = 55;
KingMove[55] = new int[5];
KingMove[55][0] = 46;
KingMove[55][1] = 54;
KingMove[55][2] = 62;
KingMove[55][3] = 47;
KingMove[55][4] = 63;
KingMove[63] = new int[3];
KingMove[63][0] = 54;
KingMove[63][1] = 62;
KingMove[63][2] = 55;

}	
	

}