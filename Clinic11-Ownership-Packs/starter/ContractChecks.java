import api.*;
import app.TripPlanner;
import legacy.LegacyPlanner;
import transfer.LongestLeg;
import extension.WaypointEdit;
import java.util.*;
import java.util.function.Supplier;
public final class ContractChecks {
    private static int checks;
    private ContractChecks() {}
    private static void eq(Object expected,Object actual,String name) {
        checks++;
        if(!Objects.equals(expected,actual))throw new AssertionError(name+" expected="+expected+" actual="+actual);
    }
    private static void error(Class<? extends RuntimeException> type,String message,Runnable action) {
        checks++;
        try {action.run();throw new AssertionError("missing "+type.getSimpleName());}
        catch(RuntimeException failure) {
            if(failure.getClass()!=type||!Objects.equals(message,failure.getMessage()))
                throw new AssertionError("wrong error",failure);
        }
    }
    private static void immutable(List<?> values) {
        checks++;
        try{values.clear();throw new AssertionError("mutable stops");}
        catch(UnsupportedOperationException expected){ /* expected */ }
    }
    private static void values() {
        error(IllegalArgumentException.class,"x",()->new Point(-101,101));
        error(IllegalArgumentException.class,"y",()->new Point(0,101));
        error(NullPointerException.class,"name",()->new RouteData(null,null));
        for(String name:List.of(""," ","a|b","a\nb","a\rb","abcdefghijklmnopqrstuvwxy"))
            error(IllegalArgumentException.class,"name",()->new RouteData(name,null));
        error(NullPointerException.class,"stops",()->new RouteData("trip",null));
        error(IllegalArgumentException.class,"stops",()->new RouteData("trip",Collections.nCopies(9,new Point(0,0))));
        List<Point> supplied=new ArrayList<>(List.of(new Point(0,0)));
        RouteData route=new RouteData(" trip ",supplied);supplied.clear();
        eq(" trip ",route.name(),"accepted name exact");eq(1,route.stops().size(),"copy list");
        immutable(route.stops());
        List<Point> bad=new ArrayList<>();bad.add(null);checks++;
        try{new RouteData("trip",bad);throw new AssertionError("null point");}
        catch(NullPointerException expected){ /* null elements are rejected */ }
    }
    private static void behavior(Supplier<PlannerAPI> make) {
        values();PlannerAPI planner=make.get();
        eq(7,planner.distance(Main.sample()),"hand route");
        eq("walk|stops=4|distance=7",planner.summary(Main.sample()),"hand summary");
        eq(400,planner.distance(new RouteData("edge",List.of(new Point(-100,100),new Point(100,-100)))),"opposing coordinate differences");
        eq(0,planner.distance(new RouteData("empty",List.of())),"empty");
        eq("one|stops=1|distance=0",planner.summary(new RouteData("one",List.of(new Point(2,3)))),"single");
        error(NullPointerException.class,"route",()->planner.distance(null));
        error(NullPointerException.class,"route",()->planner.summary(null));
        Random random=new Random(611015);
        for(int n=0;n<=8;n++)for(int trial=0;trial<35;trial++) {
            List<Point> points=new ArrayList<>();
            for(int index=0;index<n;index++)points.add(new Point(random.nextInt(201)-100,random.nextInt(201)-100));
            RouteData route=new RouteData("path",points);
            // Independent coordinate-vector calculation plus hand-derived examples.
            int[] xs=points.stream().mapToInt(Point::x).toArray(),ys=points.stream().mapToInt(Point::y).toArray();
            int expected=0;
            for(int k=1;k<n;k++)expected+=Math.abs(xs[k]-xs[k-1])+Math.abs(ys[k]-ys[k-1]);
            eq(expected,planner.distance(route),"coordinate model");
            eq("path|stops="+n+"|distance="+expected,planner.summary(route),"model summary");
            eq(points,route.stops(),"read purity");
            List<Point> reversed=new ArrayList<>(points);Collections.reverse(reversed);
            eq(expected,planner.distance(new RouteData("reverse",reversed)),"reverse preserves distance");
            eq(expected,make.get().distance(route),"independent client");
        }
    }
    private static void transfer() {
        error(NullPointerException.class,"route",()->LongestLeg.measure(null));
        eq(3,LongestLeg.measure(Main.sample()),"longest not total");
        eq(400,LongestLeg.measure(new RouteData("edge",List.of(new Point(-100,100),new Point(100,-100)))),"opposite signs");
        eq(0,LongestLeg.measure(new RouteData("empty",List.of())),"empty longest");
        eq(0,LongestLeg.measure(new RouteData("one",List.of(new Point(2,3)))),"single longest");
        for(int d=-100;d<=100;d++) {
            RouteData route=new RouteData("triangle",List.of(new Point(0,0),new Point(d,0),new Point(d,100)));
            eq(100,LongestLeg.measure(route),"largest of two legs");
            eq(List.of(new Point(0,0),new Point(d,0),new Point(d,100)),route.stops(),"longest query purity");
        }
    }
    private static void extension() {
        RouteData route=Main.sample(),changed=WaypointEdit.replace(route,1,new Point(3,4));
        eq("walk",changed.name(),"name retained");eq(4,changed.stops().size(),"size retained");
        eq(new Point(3,4),changed.stops().get(1),"replace exact index");
        eq(List.of(new Point(0,0),new Point(3,0),new Point(3,-2),new Point(1,-2)),route.stops(),"original retained");
        eq(15,new LegacyPlanner().distance(changed),"changed path");immutable(changed.stops());
        error(NullPointerException.class,"route",()->WaypointEdit.replace(null,-1,null));
        error(NullPointerException.class,"replacement",()->WaypointEdit.replace(route,-1,null));
        error(IndexOutOfBoundsException.class,"index",()->WaypointEdit.replace(route,-1,new Point(0,0)));
        error(IndexOutOfBoundsException.class,"index",()->WaypointEdit.replace(route,4,new Point(0,0)));
        error(IndexOutOfBoundsException.class,"index",()->WaypointEdit.replace(new RouteData("empty",List.of()),0,new Point(0,0)));
        for(int index=0;index<route.stops().size();index++) {
            RouteData preview=WaypointEdit.replace(route,index,new Point(100,-100));
            for(int other=0;other<route.stops().size();other++)
                eq(other==index?new Point(100,-100):route.stops().get(other),preview.stops().get(other),"position-specific replacement");
            immutable(preview.stops());
        }
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyPlanner::new);behavior(TripPlanner::new);}
            case "core" -> behavior(TripPlanner::new);
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {behavior(TripPlanner::new);transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+checks+" checks passed");
    }
}
