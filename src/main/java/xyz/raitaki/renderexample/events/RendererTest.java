package xyz.raitaki.renderexample.events;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import xyz.raitaki.renderexample.Utils.RenderUtils;
import xyz.raitaki.renderexample.Utils.VectorUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RendererTest implements WorldRenderEvents.Last {
    @Override
    public void onLast(WorldRenderContext context) {
        spawnCircleY();
        spawnCircle();
        renderFilledSphere();
        renderEmptySphere();
        renderLineSphere();

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.scale(1, 1, 1);
        Vec3d vec1 = new Vec3d(6, -59, 6);
        //RenderUtils.renderFlat(matrixStack, vec1, vec1.add(1, 0, 2), Color.GREEN);
        RenderUtils.renderFilledT(matrixStack, vec1.add(0, 0, 1), vec1.add(3, 0, 3), Color.GREEN);
        //renderFilledCircle();
        renderFilledSphere();
        rangle += 0.005;
        timer++;
        if (timer % 10 == 0) {
            timer = 0;
        }
    }

    ArrayList<Color> colors = new ArrayList<>();
    ArrayList<Color> colorsSphere = new ArrayList<>();
    double rangle = 0;
    Random rand = new Random();
    int timer = 0;
    int check = 5;

    public void spawnCircleY() {
        int particles = 100;
        Vec3d mainpos = new Vec3d(0, -59, 0);
        Vec3d lastpos = new Vec3d(2, -58.5, 2);
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(0.5f, 0.5f, 0.5f);
        float radius = 1f;
        for (int i = 1; i < particles; i++) {
            double angle, x, z;
            angle = 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            if (i % 2 == 0) {
                if (colors.size() != particles) {
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    colors.add(new Color(r, g, b, 1));
                }
                Vec3d rotatedmain = VectorUtils.rotateAroundAxisY(mainpos.add(x, 0, z), rangle);
                Vec3d rotatedlast = VectorUtils.rotateAroundAxisY(lastpos, rangle);
                //Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);
                //Vec3d rotatedlast2 = VectorUtils.rotateAroundAxisY(rotatedlast, rangle);

                RenderUtils.renderLine(positionMatrix, rotatedmain.add(-2, 0, -2), rotatedlast.add(-2, 0, -2), colors.get((i / 2) - 1));
            } else
                lastpos = mainpos.add(x, 0, z);
        }

        if (timer % check == 0) {
            changeColor(colors);
        }
        //colors.clear();
    }

    public void spawnCircle() {
        int particles = 50;
        Vec3d mainpos = new Vec3d(0, 0, 0);
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(1, 1, 1);
        float radius = 1f;
        for (int i = 0; i < particles; i++) {
            double angle, x, y;
            angle = 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            y = Math.sin(angle) * radius;

            if (colors.size() != particles) {
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                colors.add(new Color(r, g, b, 1));
            }
            Vec3d rotatedmain = VectorUtils.rotateAroundAxisX(mainpos.add(x, y, 0), rangle);
            Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);

            renderThing(rotatedmain2, colors.get((i / 2)));
            //RenderUtils.test(positionMatrix, rotatedmain2.subtract(0,59,0), rotatedlast2.subtract(0,59,0), colors.get((i/2)-1));

            mainpos.subtract(x, 0, y);
        }

        if (timer % check == 0) {
            changeColor(colors);
        }
        //colors.clear();
    }

    ArrayList<Color> colorssphereFILL = new ArrayList<>();

    public void renderLineSphere() {
        Vec3d mainpos = new Vec3d(0, 0, 0);
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(1, 1, 1);

        ArrayList<Vec3d> locs = new ArrayList<>();
        for (double i = 0; i <= Math.PI; i += Math.PI / 60) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                locs.add(mainpos.add(x, y, z));

                if (colorsSphere.size() != (60 * 10 * 2)) {
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    colorsSphere.add(new Color(r, g, b, 1));
                }
            }
        }

        for (int i = 2; i < locs.size(); i++) {
            Vec3d loc1 = locs.get(i - 1);
            Vec3d loc2 = locs.get(i);
            if (loc1.y != loc2.y) {
                loc2 = locs.get(i - 20);
            }
            Vec3d rotatedmain = VectorUtils.rotateAroundAxisX(loc1, rangle);
            Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);

            Vec3d rotatedsub = VectorUtils.rotateAroundAxisX(loc2, rangle);
            Vec3d rotatedsub2 = VectorUtils.rotateAroundAxisY(rotatedsub, rangle);

            //to make it circle change rotatedmain to \/ rotatedmain2 to make it broken change it to rotatedmai

            if (i == locs.size())
                RenderUtils.renderLine(positionMatrix, rotatedmain2.add(2, -59, 2), rotatedsub2.add(2, -59, 2), colorsSphere.get(locs.size() - 1));
            else
                RenderUtils.renderLine(positionMatrix, rotatedmain2.add(2, -59, 2), rotatedsub2.add(2, -59, 2), colorsSphere.get((i / 2) - 1));

        }

        if (timer % check == 0) {
            changeColor(colorsSphere);
        }

    }

    public void renderFilledCircle() {
        int particles = 20;
        Vec3d mainpos = new Vec3d(6, -59, 6);
        Vec3d lastpos = new Vec3d(6, -58.5, 6);
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(0.5f, 0.5f, 0.5f);
        float radius = 1f;
        for (int i = 1; i < particles; i++) {
            double angle, x, z;
            angle = 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            if (i % 2 == 0) {
                if (colors.size() != particles) {
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    colors.add(new Color(r, g, b, 1));
                }
                //Vec3d rotatedmain = VectorUtils.rotateAroundAxisY(mainpos.add(x, 0, z), rangle);
                //Vec3d rotatedlast = VectorUtils.rotateAroundAxisY(lastpos, rangle);
                //Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisY(rotatedmain, rangle);
                //Vec3d rotatedlast2 = VectorUtils.rotateAroundAxisY(rotatedlast, rangle);

                //RenderUtils.renderFilledCircle(positionMatrix, mainpos.add(x, 0, z), mainpos, mainpos, Color.green);
            } else
                lastpos = mainpos.add(x, 0, z);
        }

        if (timer % check == 0) {
            changeColor(colors);
        }
        //colors.clear();
    }

    boolean reduce = true;
    Color fillColor = null;

    public void renderEmptySphere(){
        int size = 8;
        Vec3d mainpos = new Vec3d(0, 0, 0);
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(0.5f, 0.5f, 0.5f);

        ArrayList<Vec3d> locs = new ArrayList<>();
        for (double i = 0; i <= Math.PI; i += Math.PI / size) {
            double radius = Math.sin(i)*2;
            double y = Math.cos(i)*2;
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                locs.add(mainpos.add(x, y, z));

                if (fillColor == null) {
                    float f = rand.nextFloat();
                    fillColor = new Color(f, 0, 0, 0.8f);
                }
            }
        }

        for (int i = 0; i < locs.size(); i++) {
            Vec3d loc1 = locs.get(i);
            Vec3d loc2 = locs.get(i);
            Vec3d loc3 = locs.get(i);
            Vec3d loc4 = locs.get(i);
            if (loc1.y == loc2.y) {
                if(i + 20 < locs.size())
                    loc2 = locs.get(i + 19);
                else loc2 = locs.get(locs.size()-1);
            }
            if (locs.indexOf(loc2) + 1 < locs.size())
                loc4 = locs.get(locs.indexOf(loc2) + 1);
            else loc4 = locs.get(locs.size()-2);

            if (locs.indexOf(loc1) + 1 < locs.size()) {
                loc3 = locs.get(locs.indexOf(loc1) + 1);
            }
            else loc3 = locs.get(locs.size()-2);

            Vec3d rotatedmain = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc1, rangle), rangle);
            Vec3d rotatedsub = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc2, rangle), rangle);
            Vec3d rotatedsub2 = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc3, rangle), rangle);
            Vec3d rotatedsub4 = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc4, rangle), rangle);

            RenderUtils.renderQuad(positionMatrix, rotatedmain.add(-6, -59, -6),
                    rotatedsub.add(-6, -59, -6),
                    rotatedsub2.add(-6, -59, -6), rotatedsub4.add(-6, -59, -6), fillColor);
        }

        if (timer % 10 == 0) {
            fillColor = changeColor(fillColor, 0.005f);
        }
    }

    public void renderFilledSphere() {
        int size = 8;
        Vec3d mainpos = new Vec3d(0, 0, 0);
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(0.5f, 0.5f, 0.5f);

        ArrayList<Vec3d> locs = new ArrayList<>();
        for (double i = 0; i <= Math.PI; i += Math.PI / size) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                locs.add(mainpos.add(x, y, z));

                if (fillColor == null) {
                    float f = rand.nextFloat();
                    fillColor = new Color(f, 0, 0, 0.8f);
                }
            }
        }

        for (int i = 0; i < locs.size(); i++) {
            Vec3d loc1 = locs.get(i);
            Vec3d loc2 = locs.get(i);
            Vec3d loc3 = locs.get(i);
            Vec3d loc4 = locs.get(i);
            if (loc1.y == loc2.y) {
                if(i + 20 < locs.size())
                    loc2 = locs.get(i + 19);
            }
            if (locs.indexOf(loc2) + 1 < locs.size())
                loc4 = locs.get(locs.indexOf(loc2) + 1);
            if (locs.indexOf(loc1) + 1 < locs.size()) {
                loc3 = locs.get(locs.indexOf(loc1) + 1);
            }

            Vec3d rotatedmain = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc1, rangle), rangle);
            Vec3d rotatedmain2 = VectorUtils.rotateAroundAxisX(mainpos, rangle);
            Vec3d rotatedsub = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc2, rangle), rangle);
            Vec3d rotatedsub2 = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc3, rangle), rangle);
            Vec3d rotatedsub4 = VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(loc4, rangle), rangle);

            RenderUtils.renderQuad(positionMatrix, rotatedmain.add(-4, -59, -4),
                    rotatedsub.add(-4, -59, -4),
                    rotatedsub2.add(-4, -59, -4), rotatedsub4.add(-4, -59, -4), fillColor);
            RenderUtils.renderQuad(positionMatrix, rotatedmain.add(-4, -59, -4),
                    rotatedsub.add(-4, -59, -4), rotatedmain2.add(-4, -59, -4),
                    rotatedmain2.add(-4, -59, -4), fillColor);
        }

        if (timer % 10 == 0) {
            fillColor = changeColor(fillColor, 0.005f);
        }
    }


    public void renderThing(Vec3d vec, Color color) {
        MatrixStack positionMatrix = new MatrixStack();
        positionMatrix.scale(0.5f, 0.5f, 0.5f);
        RenderUtils.renderFilled(positionMatrix, vec.subtract(0, 59, 0), new Vec3d(0.07, 0.07, 0.07), color);
    }

    public void changeColor(ArrayList<Color> list) {
        Color first = colors.get(0);

        list.remove(0);
        list.add(first);
    }

    public Color changeColor(Color color, float amount){
        Color ret = null;

        float red = color.getRed()/255f;
        float green = color.getGreen()/255f;
        float blue = color.getBlue()/255f;
        if(reduce) {
            if(red + amount > 1){
                amount = 0;
                reduce = false;
            }
            ret = new Color(red + amount, green + amount, blue + amount, color.getAlpha() / 255f);
        }
        else {
            if(red - amount < 0.5){
                amount = 0;
                reduce = true;
            }
            ret = new Color(red - amount, green - amount, blue - amount, color.getAlpha() / 255f);

        }

        return ret;
    }
}

