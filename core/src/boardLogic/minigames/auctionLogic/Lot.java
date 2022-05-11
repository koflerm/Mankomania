package boardLogic.minigames.auctionLogic;

public enum Lot {
    WAR_MEDAL{
        @Override
        public String getName(){
            return "War Medal";
        }

        @Override
        public double getPrice() {
            return 20000;
        }
    },
    SPORT_CAR{
        @Override
        public String getName(){
            return "Sport Car";
        }
        @Override
        public double getPrice() {
            return 100000;
        }
    },
    EMERALD{
        @Override
        public String getName(){
            return "Emerald";
        }
        @Override
        public double getPrice() {
            return 60000;
        }
    },
    CRYSTAL_EGG{
        @Override
        public String getName(){
            return "Crystal Egg";
        }
        @Override
        public double getPrice() {
            return 60000;
        }
    },
    ANTIQUE_CLOCK{
        @Override
        public String getName(){
            return "Antique Clock";
        }
        @Override
        public double getPrice() {
            return 40000;
        }
    },
    CHEAP_MONALISA{
        @Override
        public String getName(){
            return "Cheap Monalisa";
        }
        @Override
        public double getPrice() {
            return 60000;
        }
    },
    PEARL_NECKLACE{
        @Override
        public String getName(){
            return "Pearl Necklace";
        }
        @Override
        public double getPrice() {
            return 60000;
        }
    },
    LOST_ANTIQUE_BOOK{
        @Override
        public String getName(){
            return "Lost Antique Book";
        }
        @Override
        public double getPrice() {
            return 40000;
        }
    },
    COLLECTION_STAMP{
        @Override
        public String getName(){
            return "Collection Stamp";
        }
        @Override
        public double getPrice() {
            return 20000;
        }
    };


    public abstract String getName();
    public abstract double getPrice();
}
