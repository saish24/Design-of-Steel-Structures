    package com.example.designofsteelstructures;

    public class Design {
        private final String name;
        private final int length;
        private final int width;
        private final int thickness;
        private final Double sectionalArea;
        private final Double weightPerMeter;
        private final Double Cxx;
        private final Double Ixx;
        private final Double Iuu;
        private final Double Izz;
        private final Double Rxx;
        private final Double Ruu;
        private final Double Rzz;
        private final Double Zxx;
        private final Double Ixy;
        private final Double Cyy;
        private final Double Exx;
        private final Double Eyy;
        private final Double Iyy;
        private final Double Ryy;
        private final Double Zyy;

        public String getName() {
            return name;
        }

        public int getLength() {
            return length;
        }

        public int getWidth() {
            return width;
        }

        public int getThickness() {
            return thickness;
        }

        public Double getSectionalArea() {
            return sectionalArea;
        }

        public Double getWeightPerMeter() {
            return weightPerMeter;
        }

        public Double getCxx() {
            return Cxx;
        }

        public Double getIxx() {
            return Ixx;
        }

        public Double getIuu() {
            return Iuu;
        }

        public Double getIzz() {
            return Izz;
        }

        public Double getRxx() {
            return Rxx;
        }

        public Double getRuu() {
            return Ruu;
        }

        public Double getRzz() {
            return Rzz;
        }

        public Double getZxx() {
            return Zxx;
        }

        public Double getIxy() {
            return Ixy;
        }

        public Double getCyy() {
            return Cyy;
        }

        public Double getExx() {
            return Exx;
        }

        public Double getEyy() {
            return Eyy;
        }

        public Double getIyy() {
            return Iyy;
        }

        public Double getRyy() {
            return Ryy;
        }

        public Double getZyy() {
            return Zyy;
        }

        //    Constructor for equal angles
        public Design(String name, int length, int width, int thickness, double sectionalArea, double weightPerMeter, double Cxx,
                      double Ixx, double Iuu, double Izz, double Rxx, double Ruu, double Rzz, double Zxx, double Ixy) {
            this.name = name;
            this.length = length;
            this.width = width;
            this.thickness = thickness;
            this.sectionalArea = sectionalArea;
            this.weightPerMeter = weightPerMeter;
            this.Cxx = Cxx;
            this.Cyy = Cxx;
            this.Ixx = Ixx;
            this.Iuu = Iuu;
            this.Izz = Izz;
            this.Rxx = Rxx;
            this.Ryy = Rxx;
            this.Ruu = Ruu;
            this.Rzz = Rzz;
            this.Zxx = Zxx;
            this.Zyy = Zxx;
            this.Iyy = Ixx;
            this.Ixy = Ixy;
            this.Exx = 0d;
            this.Eyy = 0d;
        }

        //    Constructor for unequal angles
        public Design(String name, int length, int width, int thickness, double sectionalArea, double weightPerMeter, double Cxx,
                      double Cyy, double Exx, double Eyy, double Ixx, double Iyy, double Iuu, double Izz, double Rxx, double Ryy,
                      double Ruu, double Rzz, double Zxx, double Zyy, double Ixy) {
            this.name = name;
            this.length = length;
            this.width = width;
            this.thickness = thickness;
            this.sectionalArea = sectionalArea;
            this.weightPerMeter = weightPerMeter;
            this.Cxx = Cxx;
            this.Exx = Exx;
            this.Ixx = Ixx;
            this.Iuu = Iuu;
            this.Izz = Izz;
            this.Rxx = Rxx;
            this.Ruu = Ruu;
            this.Rzz = Rzz;
            this.Zxx = Zxx;
            this.Ixy = Ixy;
            this.Cyy = Cyy;
            this.Eyy = Eyy;
            this.Iyy = Iyy;
            this.Ryy = Ryy;
            this.Zyy = Zyy;

        }
    }
