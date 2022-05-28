package boardLogic;

import fieldLogic.Field;

public class BoardFields {

    private static Field[] initFields(float boardX, float boardLength) {
        return new Field[]{
                new Field(0, "Dein Buch \"Geld verjubeln leicht gemacht\" ist ein Bestseller! Kassiere 5000 Euro",boardX + (boardLength / 1.135f), boardLength / 15),
                new Field(1, "Verwöhne dich bei Juwelier \"Bling Bling & Bling\". Zahle 170.000 Euro.",boardX + (boardLength / 14f), boardLength / 8.5f),
                new Field(2, "Gönne dir eine goldene Handyhülle. Zahle 50.000 Euro.",boardX + (boardLength / 8.5f), boardLength / 1.08f),
                new Field(3, "Deine Brieftasche ist zu schwer! Zahle 100.000",boardX + (boardLength / 1.08f), boardLength / 1.13f),
                new Field(4, "Deine Brieftasche ist zu schwer! Zahle 50.000",boardX + (boardLength / 1.22f), boardLength / 23),
                new Field(5, "Du bist der ein millionste Kunden bei \"Edel und Teuer\". Du gewinnst 10000 Euro.",boardX + (boardLength / 1.33f), boardLength / 19),
                new Field(6, "Du kaufst nutzlose Aktien. Zahle 100.000 Euro.",boardX + (boardLength / 1.45f), boardLength / 12),
                new Field(7, "Du kaufst Lotterielose ohne Gewinn. Zahle 50.000 Euro",boardX + (boardLength / 1.53f), boardLength / 7),
                new Field(8, "Gehe zur Rennstrecke.",boardX + (boardLength / 1.545f), boardLength / 5),
                new Field(9, "Mit deinem Bild \"Malen nach Zahlen\" gewinnst du den Turner Prize. Kassiere 10.000 Euro",boardX + (boardLength / 1.53f), boardLength / 3.9f),
                new Field(10, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 1.449f), boardLength / 3.2f),
                new Field(11, "Alles Gute zum Geburtstag! Du erhälst 5.000 Euro",boardX + (boardLength / 1.35f), boardLength / 2.9f),
                new Field(12, "Du kaufst nutzlose Aktien. Zahle 100.000 Euro.",boardX + (boardLength / 1.71f), boardLength / 5),
                new Field(13, "Du kaufst Lotterielose ohne Gewinn. Zahle 50.000 Euro",boardX + (boardLength / 2f), boardLength / 5),
                new Field(14, "Du kaufst nutzlose Aktien. Zahle 50.000 Euro.",boardX + (boardLength / 2.43f), boardLength / 5),
                new Field(15, "Dein Personal Trainer nimmt dich zu hart ran. Gehe zur Osteopathen. Zahle 50.000 Euro.",boardX + (boardLength / 2.83f), boardLength / 5),
                new Field(16, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 2.90f), boardLength / 7),
                new Field(17, "Deine Eltern haben Hochzeitstag! Überrasche Sie mit einer VIP-Weltreise. Zahle 100.000 Euro",boardX + (boardLength / 3.25f), boardLength / 11.5f),
                new Field(18, "Du kaufst Lotterielose ohne Gewinn. Zahle 50.000 Euro",boardX + (boardLength / 4f), boardLength / 19),
                new Field(19, "Exklusive Fotoshooting für ein Hochzeitsmagazin Kassiere 50.000 Euro.",boardX + (boardLength / 5.5f), boardLength / 23),
                new Field(20, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 9f), boardLength / 15),
                new Field(21, "Du kaufst nutzlose Aktien. Zahle 50.000 Euro.",boardX + (boardLength / 22f), boardLength / 5.5f),
                new Field(22, "Titel gewonnen! \"Begossenster Pudel des Jahres\" bei der World Dog Show. Kassiere 10.000 Euro",boardX + (boardLength / 20f), boardLength / 4f),
                // MINIGAME BÖRSE
                new Field(23, "Börse",boardX + (boardLength / 12f), boardLength / 3.3f),
                new Field(24, "Deine Eltern haben Hochzeitstag! Überrasche Sie mit einer VIP-Weltreise. Zahle 100.000 Euro",boardX + (boardLength / 7f), boardLength / 2.9f),
                new Field(25, "Dein Personal Trainer nimmt dich zu hart ran. Gehe zur Osteopathen. Zahle 50.000 Euro.",boardX + (boardLength / 4.95f), boardLength / 2.85f),
                new Field(26, "Hamsterkauf! Besorge einen Jahresvorrat Klopapier aus 4.lagiger Seide. Zahle 40.000 Euro.",boardX + (boardLength / 4.95f), boardLength / 2.4f),
                new Field(27, "Dein Personal Trainer nimmt dich zu hart ran. Gehe zur Osteopathen. Zahle 50.000 Euro.",boardX + (boardLength / 4.95f), boardLength / 2.14f),
                new Field(28, "Beauftragte Saylor Twift damit, dir einen Klingelton zu komponieren. Zahle 70.000 Euro",boardX + (boardLength / 4.95f), boardLength / 1.89f),
                new Field(29, "Dein Personal Trainer nimmt dich zu hart ran. Gehe zur Osteopathen. Zahle 50.000 Euro.",boardX + (boardLength / 4.95f), boardLength / 1.7f),
                new Field(30, "Du bekommst einen heißen Tipp! Gehe zur Rennstrecke",boardX + (boardLength / 4.95f), boardLength / 1.55f),
                new Field(31, "Kaufe eine Discokugel aus Diamanten. Zahle 150.000 Euro",boardX + (boardLength / 7f), boardLength / 1.53f),
                new Field(32, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 12f), boardLength / 1.45f),
                new Field(33, "Verwöhne deinen Hund mit einem Halsband aus Rubinen. Zahle 30.000 Euro",boardX + (boardLength / 20f), boardLength / 1.33f),
                new Field(34, "Gehe zur Rennstrecke",boardX + (boardLength / 22f), boardLength / 1.22f),
                new Field(35, "Verwöhne deinen Hund mit einem Halsband aus Rubinen. Zahle 30.000 Euro",boardX + (boardLength / 14f), boardLength / 1.13f),
                new Field(36, "Du bist auf einem Kaviarschnittchen ausgerutscht. Kassiere 10.000 Euro",boardX + (boardLength / 5.5f), boardLength / 1.05f),
                new Field(37, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 4f), boardLength / 1.06f),
                new Field(38, "Spendierhosen in der Bar! Zahle 5.000 Euro",boardX + (boardLength / 3.33f), boardLength / 1.105f),
                new Field(39, "Kaufe Anteile an einem Rennpferd! Zahle 50.000 Euro.",boardX + (boardLength / 2.90f), boardLength / 1.17f),
                new Field(40, "Kaufe Anteile an nachhaltigen Kohlebergwerken! Zahle 50.000 Euro.",boardX + (boardLength / 2.83f), boardLength / 1.26f),
                new Field(41, "Du verletzt dich bei einem Wrestlingmatch! Du erhälst 20.000 Euro.",boardX + (boardLength / 2.90f), boardLength / 1.36f),
                // MINIGAME AUKTIONSHAUS
                new Field(42, "Aktionshaus",boardX + (boardLength / 3.25f), boardLength / 1.455f),
                new Field(43, "Deine Oma benötigt ein neues Facelifting. Zahle 80.000 Euro",boardX + (boardLength / 3.9f), boardLength / 1.52f),
                new Field(44, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 2.43f), boardLength / 1.26f),
                new Field(45, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 2.13f), boardLength / 1.26f),
                new Field(46, "Du steckst deine Ersparnisse in Bitcoin. Zahle 100.000 Euro.",boardX + (boardLength / 1.9f), boardLength / 1.26f),
                new Field(47, "Heute ist nicht dein Tag! Zahle 100.000 Euro.",boardX + (boardLength / 1.71f), boardLength / 1.26f),
                new Field(48, "Du steckst deine Ersparnisse in Bitcoin. Zahle 100.000 Euro.",boardX + (boardLength / 1.545f), boardLength / 1.26f),
                new Field(49, "Du steckst deine Ersparnisse in Etherium. Zahle 100.000 Euro.",boardX + (boardLength / 1.53f), boardLength / 1.17f),
                new Field(50, "Du steckst deine Ersparnisse in DogeCoin. Zahle 100.000 Euro.",boardX + (boardLength / 1.45f), boardLength / 1.105f),
                new Field(51, "Du steckst deine Ersparnisse in NFTs. Zahle 100.000 Euro.",boardX + (boardLength / 1.33f), boardLength / 1.06f),
                new Field(52, "Du steckst deine Ersparnisse in ein Schneeballsystem. Zahle 100.000 Euro.",boardX + (boardLength / 1.22f), boardLength / 1.05f),
                new Field(53, "Deine Mutter klaut dein Geld für eine neue Handtasche. Zahle 20.000 Euro.",boardX + (boardLength / 1.14f), boardLength / 1.08f),
                new Field(54, "Verwöhne dein Pony mit Beinwärmern aus Kaschmir! Zahle 50.000 Euro.",boardX + (boardLength / 1.055f), boardLength / 1.22f),
                new Field(55, "",boardX + (boardLength / 1.06f), boardLength / 1.33f),
                // MINIGAME RENNSTRECKE
                new Field(56, "Rennstrecke",boardX + (boardLength / 1.1f), boardLength / 1.44f),
                new Field(57, "Du bist auf einem Kaviarschnittchen ausgerutscht. Kassiere 10.000 Euro",boardX + (boardLength / 1.17f), boardLength / 1.52f),
                new Field(58, "Gönn dir einen mit Champagner gefüllten Jacuzzi. Zahle 60.000 Euro.",boardX + (boardLength / 1.255f), boardLength / 1.56f),
                new Field(59, "Lege mit deiner Yacht im Hafen von Monaco an. Zahle 50.000 Euro.",boardX + (boardLength / 1.255f), boardLength / 1.71f),
                new Field(60, "Du klaust den Hut eines Bettlers. Du erhälst 50 Euro.",boardX + (boardLength / 1.255f), boardLength / 1.9f),
                new Field(61, "Du kaufst nutzlose Aktien. Zahle 100.000 Euro.",boardX + (boardLength / 1.255f), boardLength / 2.13f),
                new Field(62, "Spendiere einem Spieler deiner Wahl eine Verjüngerungskur Zahle 100.000 Euro.",boardX + (boardLength / 1.255f), boardLength / 2.43f),
                new Field(63, "Du versenkst deine Yacht in Lignano. Zahle 200.000 Euro.",boardX + (boardLength / 1.255f), boardLength / 2.85f),
                new Field(64, "Verwöhne dein Pony mit Beinwärmern aus Kaschmir! Zahle 50.000 Euro.",boardX + (boardLength / 1.17f), boardLength / 2.9f),
                new Field(65, "Gönne dir eine goldene Handyhülle. Zahle 50.000 Euro.",boardX + (boardLength / 1.1f), boardLength / 3.3f),
                new Field(66, "Heute ist nicht dein Tag! Zahle 10.000 Euro.",boardX + (boardLength / 1.06f), boardLength / 4f),
                new Field(67, "Dein Hamster stirbt und die Nagetierversicherung zahlt. Kassiere 10.000 Euro",boardX + (boardLength / 1.055f), boardLength / 5.5f),
                new Field(68, "Du kaufst nutzlose Aktien. Zahle 100.000 Euro.",boardX + (boardLength / 1.08f), boardLength / 9f)
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
