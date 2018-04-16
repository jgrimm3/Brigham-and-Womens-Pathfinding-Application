package com.manlyminotaurs.pathfinding;

import com.manlyminotaurs.nodes.Node;

import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class PathfinderUtil {
    //Unicode arrows

    char lArrow[] = Character.toChars(129092);
    String leftArrow = new String(lArrow);
    char rArrow[] = Character.toChars(129094);
    String rightArrow = new String(rArrow);
    char slArrow[] = Character.toChars(129184);
    String slightLeftArrow = new String(slArrow);
    char sRArrow[] = Character.toChars(129184);
    String slightRightArrow = new String(sRArrow);
    char straight[] = Character.toChars(129093);
    String straightArrow = new String(straight);

    /**
     * calculates angle from 3 Nodes
     *
     * @param node1: Node 1
     * @param node2: Node 2
     * @param node3: Node 3
     * @return angle between node 1 and node 3
     */

    private int calcAngle(Node node1, Node node2, Node node3) {
        double n1x = node1.getXCoord();
        double n1y = node1.getYCoord();
        double n2x = node2.getXCoord();
        double n2y = node2.getYCoord();
        double n3x = node3.getXCoord();
        double n3y = node3.getYCoord();
        double angle1 = Math.atan2(n1y - n2y, n1x - n2x);
        double angle2 = Math.atan2(n2y - n3y, n2x - n3x);
        double finalAngle = (angle2 - angle1) * (180 / Math.PI);
        if (finalAngle < 0) { finalAngle += 360; }
        System.out.println("Angle: " + (int) finalAngle + " Degrees");
        return (int) finalAngle;
    }

    /**
     * Completes each turn by turn direction
     *
     * @param node: node
     * @return String containing preposition (onto/to) and node name
     */

    private String nameToString(Node node) {
        return " at " + node.getLongName();
    }

    /**
     * creates string of distance to append to tbt directions
     *
     * @param startNode: starting node
     * @param endNode: ending node
     * @return distance, ex: "in 500 ft, "...
     */
    private String nodesToDistance(Node startNode, Node endNode) {
        double distance = CalcDistance.calcDistance(startNode, endNode);
        String strDouble = String.format("%.2f", distance);
        return "in " + strDouble + " ft, ";
    }


    /**
     * associates direction to corresponding angle for each node in list
     *
     * @param path: List of Nodes
     * @return Array of Strings containing directions
     */

    public ArrayList<String> angleToText(LinkedList<Node> path) {
        ArrayList<String> tbt = new ArrayList<>();
        /* check for <= 2 node path */
        if (path.size() <= 2) { tbt.add(straightArrow + nameToString(path.getLast())); return tbt; }
        /* loop through path */
        for (int i = 0; i < path.size() - 2; i++) {
            System.out.println("Intersection: " + (i + 1));
            double angle = calcAngle(path.get(i), path.get((i+1)), path.get((i+1)));
            if (angle > 30 && angle <= 45) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + "make a slight right" + nameToString(path.get((i+1))));
            } else if (angle > 45 && angle <= 135) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + rightArrow + nameToString(path.get((i+1))));
            } else if (angle > 135 && angle <= 175) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + "make a sharp right" + nameToString(path.get((i+1))));
            } else if (angle > 175 && angle <= 185) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + "turn around" + nameToString(path.get((i+1))));
            } else if (angle > 185 && angle <= 225) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + "make a sharp left" + nameToString(path.get((i+1))));
            } else if (angle > 225 && angle <= 315) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + leftArrow + nameToString(path.get((i+1))));
            } else if (angle > 315 && angle <= 330) {
                tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + "make a slight left" + nameToString(path.get((i+1))));
            } else { tbt.add(nodesToDistance(path.get(i), path.get(i+1)) + straightArrow + nameToString(path.get((i+1)))); }
        }
        tbt.add("Arrive" + nameToString(path.getLast()));
        return tbt;
    }

    /**
     * @author Crunchify.com
     * Updated: 03/20/2016 - added code to narrow border size
     */
    public void generateQR(ArrayList<String> directions) {
        String path = "";
        for (int i=0; i<directions.size(); i++) {
            path = path + directions.get(i) + "\n";
        }
        String myCodeText = path;
        //String filePath = "./src/main/resources/QR/CrunchifyQR.png";
        String filePath = "/QR/CrunchifyQR.png";
        int size = 250;
        String fileType = "png";
        File myFile = new File(getClass().getResource(filePath).toString());

        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
                    size, hintMap);
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
      //      ImageIO.write(image, fileType, myFile);
        } catch (WriterException e) {
            e.printStackTrace();
        } /*catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("\n\nYou have successfully created QR Code.");
    }

}

