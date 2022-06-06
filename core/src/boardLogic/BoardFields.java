package boardLogic;

import fieldLogic.Field;

public class BoardFields {

    public BoardFields() {
    }

    private static Field[] initFields(float boardX, float boardLength) {
        return new Field[]{
                new Field(0, boardX + (boardLength / 1.135f), boardLength / 15),
                new Field(1, boardX + (boardLength / 14f), boardLength / 8.5f),
                new Field(2, boardX + (boardLength / 8.5f), boardLength / 1.08f),
                new Field(3, boardX + (boardLength / 1.08f), boardLength / 1.13f),
                new Field(4, boardX + (boardLength / 1.22f), boardLength / 23),
                new Field(5, boardX + (boardLength / 1.33f), boardLength / 19),
                new Field(6, boardX + (boardLength / 1.45f), boardLength / 12),
                new Field(7, boardX + (boardLength / 1.53f), boardLength / 7),
                new Field(8, boardX + (boardLength / 1.545f), boardLength / 5),
                new Field(9, boardX + (boardLength / 1.53f), boardLength / 3.9f),
                new Field(10, boardX + (boardLength / 1.449f), boardLength / 3.2f),
                new Field(11, boardX + (boardLength / 1.35f), boardLength / 2.9f),
                new Field(12, boardX + (boardLength / 1.71f), boardLength / 5),
                new Field(13, boardX + (boardLength / 2f), boardLength / 5),
                new Field(14, boardX + (boardLength / 2.43f), boardLength / 5),
                new Field(15, boardX + (boardLength / 2.83f), boardLength / 5),
                new Field(16, boardX + (boardLength / 2.90f), boardLength / 7),
                new Field(17, boardX + (boardLength / 3.25f), boardLength / 11.5f),
                new Field(18, boardX + (boardLength / 4f), boardLength / 19),
                new Field(19, boardX + (boardLength / 5.5f), boardLength / 23),
                new Field(20, boardX + (boardLength / 9f), boardLength / 15),
                new Field(21, boardX + (boardLength / 22f), boardLength / 5.5f),
                new Field(22, boardX + (boardLength / 20f), boardLength / 4f),
                new Field(23, boardX + (boardLength / 12f), boardLength / 3.3f),
                new Field(24, boardX + (boardLength / 7f), boardLength / 2.9f),
                new Field(25, boardX + (boardLength / 4.95f), boardLength / 2.85f),
                new Field(26, boardX + (boardLength / 4.95f), boardLength / 2.4f),
                new Field(27, boardX + (boardLength / 4.95f), boardLength / 2.14f),
                new Field(28, boardX + (boardLength / 4.95f), boardLength / 1.89f),
                new Field(29, boardX + (boardLength / 4.95f), boardLength / 1.7f),
                new Field(30, boardX + (boardLength / 4.95f), boardLength / 1.55f),
                new Field(31, boardX + (boardLength / 7f), boardLength / 1.53f),
                new Field(32, boardX + (boardLength / 12f), boardLength / 1.45f),
                new Field(33, boardX + (boardLength / 20f), boardLength / 1.33f),
                new Field(34, boardX + (boardLength / 22f), boardLength / 1.22f),
                new Field(35, boardX + (boardLength / 14f), boardLength / 1.13f),
                new Field(36, boardX + (boardLength / 5.5f), boardLength / 1.05f),
                new Field(37, boardX + (boardLength / 4f), boardLength / 1.06f),
                new Field(38, boardX + (boardLength / 3.33f), boardLength / 1.105f),
                new Field(39, boardX + (boardLength / 2.90f), boardLength / 1.17f),
                new Field(40, boardX + (boardLength / 2.83f), boardLength / 1.26f),
                new Field(41, boardX + (boardLength / 2.90f), boardLength / 1.36f),
                new Field(42, boardX + (boardLength / 3.25f), boardLength / 1.455f),
                new Field(43, boardX + (boardLength / 3.9f), boardLength / 1.52f),
                new Field(44, boardX + (boardLength / 2.43f), boardLength / 1.26f),
                new Field(45, boardX + (boardLength / 2.13f), boardLength / 1.26f),
                new Field(46, boardX + (boardLength / 1.9f), boardLength / 1.26f),
                new Field(47, boardX + (boardLength / 1.71f), boardLength / 1.26f),
                new Field(48, boardX + (boardLength / 1.545f), boardLength / 1.26f),
                new Field(49, boardX + (boardLength / 1.53f), boardLength / 1.17f),
                new Field(50, boardX + (boardLength / 1.45f), boardLength / 1.105f),
                new Field(51, boardX + (boardLength / 1.33f), boardLength / 1.06f),
                new Field(52, boardX + (boardLength / 1.22f), boardLength / 1.05f),
                new Field(53, boardX + (boardLength / 1.14f), boardLength / 1.08f),
                new Field(54, boardX + (boardLength / 1.055f), boardLength / 1.22f),
                new Field(55, boardX + (boardLength / 1.06f), boardLength / 1.33f),
                new Field(56, boardX + (boardLength / 1.1f), boardLength / 1.44f),
                new Field(57, boardX + (boardLength / 1.17f), boardLength / 1.52f),
                new Field(58, boardX + (boardLength / 1.255f), boardLength / 1.56f),
                new Field(59, boardX + (boardLength / 1.255f), boardLength / 1.71f),
                new Field(60, boardX + (boardLength / 1.255f), boardLength / 1.9f),
                new Field(61, boardX + (boardLength / 1.255f), boardLength / 2.13f),
                new Field(62, boardX + (boardLength / 1.255f), boardLength / 2.43f),
                new Field(63, boardX + (boardLength / 1.255f), boardLength / 2.85f),
                new Field(64, boardX + (boardLength / 1.17f), boardLength / 2.9f),
                new Field(65, boardX + (boardLength / 1.1f), boardLength / 3.3f),
                new Field(66, boardX + (boardLength / 1.06f), boardLength / 4f),
                new Field(67, boardX + (boardLength / 1.055f), boardLength / 5.5f),
                new Field(68, boardX + (boardLength / 1.08f), boardLength / 9f)
        };
    }
    public static Field[] getFields(float boardX, float boardLength) {
        Field[] fields = initFields(boardX, boardLength);
        connectFields(fields);
        return fields;
    }

    public static void connectFields(Field[] fields) {
        fields[0].setNextField(fields[4]);
        fields[0].setPreviousField(fields[68]);
        fields[1].setNextField(fields[21]);
        fields[1].setPreviousField(fields[20]);
        fields[2].setNextField(fields[36]);
        fields[2].setPreviousField(fields[35]);
        fields[3].setNextField(fields[54]);
        fields[3].setPreviousField(fields[53]);
        for (int i = 4; i < fields.length; i++) {
            switch(i){
                case 4: // Start Position 1
                    fields[i].setNextField(fields[i+1]);
                    fields[i].setPreviousField(fields[0]);
                    break;
                case 8: // Intersection 1 First 1
                    fields[i].setNextField(fields[12]);
                    fields[i].setPreviousField(fields[i-1]);
                    break;
                case 9: case 10: case 41: case 42: // Intersection Fields
                    fields[i].setNextField(fields[i-1]);
                    fields[i].setPreviousField(fields[i+1]);
                    break;
                case 11: // Intersection 1 First 1
                    fields[i].setNextField(fields[i-1]);
                    fields[i].setPreviousField(fields[63]);
                    break;
                case 43: // Intersection 2 First 1
                    fields[i].setNextField(fields[i-1]);
                    fields[i].setPreviousField(fields[30]);
                    break;
                case 20: // Start Position 2
                    fields[i].setNextField(fields[1]);
                    fields[i].setPreviousField(fields[i-1]);
                    break;
                case 21: // Start Position 2
                    fields[i].setNextField(fields[i+1]);
                    fields[i].setPreviousField(fields[1]);
                    break;
                case 30: // Intersection 1
                    fields[i].setNextField(fields[31]);
                    fields[i].setOptionalNextField(fields[43]);
                    break;
                case 35: // Start Position 3
                    fields[i].setNextField(fields[2]);
                    fields[i].setPreviousField(fields[i-1]);
                    break;
                case 36: // Start Position 3
                    fields[i].setNextField(fields[i+1]);
                    fields[i].setPreviousField(fields[2]);
                    break;
                case 40: // Intersection 1 First 1
                    fields[i].setNextField(fields[44]);
                    fields[i].setPreviousField(fields[i-1]);
                    break;
                case 53: // Start Position 4
                    fields[i].setNextField(fields[3]);
                    fields[i].setPreviousField(fields[i-1]);
                    break;
                case 54: // Start Position 4
                    fields[i].setNextField(fields[i+1]);
                    fields[i].setPreviousField(fields[3]);
                    break;
                case 63: // Intersection 2
                    fields[i].setNextField(fields[i+1]);
                    fields[i].setOptionalNextField(fields[11]);
                    break;
                case 68: // Start Position 1
                    fields[i].setNextField(fields[0]);
                    fields[i].setPreviousField(fields[i-1]);
                    break;
                default:
                    fields[i].setPreviousField(fields[i-1]);
                    fields[i].setNextField(fields[i+1]);
                    break;
            }
        }
    }
}
