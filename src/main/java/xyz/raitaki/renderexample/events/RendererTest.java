package xyz.raitaki.renderexample.events;

import me.x150.renderer.renderer.Renderer3d;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import xyz.raitaki.renderexample.Utils.RenderUtils;
import xyz.raitaki.renderexample.Utils.VectorUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RendererTest implements WorldRenderEvents.Last {
    @Override
    public void onLast(WorldRenderContext context) {
        spawnCircleY();
        spawnCircle();
        renderSphere();

        rangle+= 0.005;
        timer++;

        if(timer % 10 == 0){
            timer = 0;
        }
    }

    ArrayList<Color> colors = new ArrayList<>();
    ArrayList<Color> colorsSphere = new ArrayList<>();
    double rangle = 0;
    Random rand = new Random();
    int timer = 0;

    public void spawnCircleY(){
        int particles = 100;
        Vec3d mainpos = new Vec3d(0, -59,0);
        Vec3d lastpos = new Vec3d(2,-58.5,2);
        MatrixStack positionMatrix =  new MatrixStack();
        positionMatrix.scale(0.5f,0.5f,0.5f);
        float radius = 1f;
        for (int i = 1; i < particles; i++) {
            double angle, x, z;
            angle = 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            if(i % 2 == 0) {
                if(colors.size() != particles) {
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    colors.add(new Color(r,g,b, 1));
                }
                Vec3d rotatedmain = VectorUtils.rotateAroundAxisY(mainpos.add(x, 0, z), rangle);
                Vec3d rotatedlast = VectorUtils.rotateAroundAxisY(lastpos, rangle);
                //Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);
                //Vec3d rotatedlast2 = VectorUtils.rotateAroundAxisY(rotatedlast, rangle);

                RenderUtils.renderLine(positionMatrix, rotatedmain.add(-2,0,-2), rotatedlast.add(-2,0,-2), colors.get((i/2)-1));
            }
            else
                lastpos = mainpos.add(x, 0, z);
        }

        if(timer % 10 == 0){
            changeColor(colors);
        }
        //colors.clear();
    }
    public void spawnCircle(){
        int particles = 50;
        Vec3d mainpos = new Vec3d(0,0,0);
        MatrixStack positionMatrix =  new MatrixStack();
        positionMatrix.scale(1,1,1);
        float radius = 1f;
        for (int i = 0; i < particles; i++) {
            double angle, x, y;
            angle = 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            y = Math.sin(angle) * radius;

            if(colors.size() != particles) {
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                colors.add(new Color(r,g,b, 0.5f));
            }
            Vec3d rotatedmain = VectorUtils.rotateAroundAxisX(mainpos.add(x, y, 0), rangle);
            Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);

            renderThing(rotatedmain2, colors.get((i/2)));
            //RenderUtils.test(positionMatrix, rotatedmain2.subtract(0,59,0), rotatedlast2.subtract(0,59,0), colors.get((i/2)-1));

            mainpos.subtract(x, 0, y);
        }

        if(timer % 10 == 0){
            changeColor(colors);
        }
        //colors.clear();
    }

    public void renderSphere(){
        Vec3d mainpos = new Vec3d(0,0,0);
        MatrixStack positionMatrix =  new MatrixStack();
        positionMatrix.scale(1,1,1);

        ArrayList<Vec3d> locs = new ArrayList<>();
        for (double i = 0; i <= Math.PI; i += Math.PI / 30) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                locs.add(mainpos.add(x,y,z));

                if(colorsSphere.size() != 600) {
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    colorsSphere.add(new Color(r,g,b, 0.5f));
                }
            }
        }

        for(int i = 1; i < locs.size(); i++){
            Vec3d loc1 = locs.get(i-1);
            Vec3d loc2 = locs.get(i);
            if(loc1.y != loc2.y) {
                 loc2 = locs.get(i-20);
            }
            Vec3d rotatedmain = VectorUtils.rotateAroundAxisX(loc1, rangle);
            Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);

            Vec3d rotatedsub = VectorUtils.rotateAroundAxisX(loc2, rangle);
            Vec3d rotatedsub2 = VectorUtils.rotateAroundAxisY(rotatedsub, rangle);

            //to make it circle change rotatedmain to \/ rotatedmain2 to make it broken change it to rotatedmain
            RenderUtils.renderLine(positionMatrix, rotatedmain2.add(2,-59,2), rotatedsub2.add(2,-59,2), colorsSphere.get(i/2));
        }

        if(timer % 10 == 0){
            changeColor(colorsSphere);
        }
    }

    public void renderThing(Vec3d vec, Color color){
        MatrixStack positionMatrix =  new MatrixStack();
        positionMatrix.scale(0.5f,0.5f,0.5f);
        RenderUtils.renderFilled(positionMatrix, vec.subtract(0,59,0), new Vec3d(0.07,0.07,0.07), color);
    }

    public void changeColor(ArrayList<Color> list){
        Color first = colors.get(0);

        list.remove(0);
        list.add(first);
    }

}
