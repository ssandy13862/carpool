package com.example.dec23_carpool.util;


import com.example.dec23_carpool.util.RoutingPath.Route.Legs.Steps.*;

import java.util.List;


public class RoutingPath {

    private List<Route> routes;

    public class Route {

        private List<Legs> legs;

        public class Legs {

            private Distance distance;
            private Duration duration;
            private String start_address, end_address;
            private Location start_location, end_location;
            private List<Steps> steps;

            public Distance getDistance() {
                return distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public String getStart_address() {
                return start_address;
            }

            public String getEnd_address() {
                return end_address;
            }

            public Location getStart_location() {
                return start_location;
            }

            public Location getEnd_location() {
                return end_location;
            }

            public List<Steps> getSteps() {
                return steps;
            }

            public class Location {
                private String lat;
                private String lng;

                public String getLat() {
                    return lat;
                }

                public String getLng() {
                    return lng;
                }
            }

            public class Steps {

                private Distance distance;
                private Duration duration;
                private Polyline polyline;

                public Distance getDistance() {
                    return distance;
                }

                public Duration getDuration() {
                    return duration;
                }

                public Polyline getPolyline() {
                    return polyline;
                }

                public class Polyline {
                    private String points;

                    public String getPoints() {
                        return points;
                    }
                }

                public class Distance {
                    private String text;

                    public String getText() {
                        return text;
                    }

                }

                public class Duration {
                    private String text;

                    public String getText() {
                        return text;
                    }

                }
            }

        }

        public List<Legs> getLegs() {
            return legs;
        }

    }

    public List<Route> getRoutes() {
        return routes;
    }
}
