package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.ScoredNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
//package com.manlyminotaurs.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
     * associates direction to corresponding angle for each node in list
     *
     * @param path: List of Nodes
     * @return Array of Strings containing directions
     */

    public ArrayList<String> angleToText(LinkedList<Node> path) {
        ArrayList<String> tbt = new ArrayList<>();
        /* check for <= 2 node path */
        if (path.size() <= 2) { tbt.add("Go straight" + nameToString(path.getLast())); return tbt; }
        /* loop through path */
        for (int i = 0; i < path.size() - 2; i++) {
            System.out.println("Intersection: " + (i + 1));
            double angle = calcAngle(path.get(i), path.get((i+1)), path.get((i+2)));
            if (angle > 30 && angle <= 45) {
                tbt.add("Make a slight right" + nameToString(path.get((i+2))));
            } else if (angle > 45 && angle <= 135) {
                tbt.add("Turn right" + nameToString(path.get((i+2))));
            } else if (angle > 135 && angle <= 175) {
                tbt.add("Make a sharp right" + nameToString(path.get((i+2))));
            } else if (angle > 175 && angle <= 185) {
                tbt.add("Turn around" + nameToString(path.get((i+2))));
            } else if (angle > 185 && angle <= 225) {
                tbt.add("Make a sharp left" + nameToString(path.get((i+2))));
            } else if (angle > 225 && angle <= 315) {
                tbt.add("Turn left" + nameToString(path.get((i+2))));
            } else if (angle > 315 && angle <= 330) {
                tbt.add("Make a slight left" + nameToString(path.get((i+2))));
            } else { tbt.add("Continue straight" + nameToString(path.get((i+2)))); }
        }
        return tbt;
    }

    public void generateQR(ArrayList<String> directions) {
        String path = "";
        for (int i=0; i<directions.size(); i++) {
            path = path + directions.get(i) + "\n";
        }
        String myCodeText = path;
        String filePath = "./src/main/resources/QR/CrunchifyQR.png";
        int size = 250;
        String fileType = "png";
        File myFile = new File(filePath);
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
            ImageIO.write(image, fileType, myFile);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nYou have successfully created QR Code.");
    }

}

