
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PaintingBoard {

    //region GUI Components
    public final static String title = "Hsy's PaintingBoard";//����
    private int width = 1024;//���ڿ��
    private int height = 768;//���ڸ߶�
    private JFrame frame;
    private JPanel panel_buttons;
    private JPanel panel_canvas;
//    private Canvas

    private JButton button_pen;
    private JButton button_eraser;
    private JButton button_drawLine;
    private JButton button_drawCircle;
    private JButton button_drawTriangle;
    private JButton button_drawRectangle;
    private JButton button_drawStar;
    private List<JButton> buttonToolGroup;

    private JButton button_clear;
    private JButton button_save;

    private BufferedImage canvasBuffer;
    private BufferedImage tempBufferedImage;
    private Point startPoint;//��¼��ͼ��ʼ��
    private Point endPoint;//��ͼ������
    private Mode mode;//��ǰѡ��滭ģʽ
    private int lineThick = 5;//������ϸֱ��
    private Color lineColor = Color.red;//������ɫ
    private int eraserThick = 50;//��Ƥ��ϸֱ��
    //endregion

    public PaintingBoard() {
        initUI();
        initCanvasListeners();
        initButtonListeners();
    }

    /*
     * ��ʼ��UI������JFrame��Button��JPanel��
     * */
    private void initUI() {
        //region init components
        frame = new JFrame();
        panel_buttons = new JPanel();
        panel_canvas = new JPanel();
        mode = Mode.NULL;
        //endregion

        //region init buttons
        buttonToolGroup = new ArrayList<>();
        button_pen = new JButton();
        button_eraser = new JButton();
        button_drawLine = new JButton();
        button_drawCircle = new JButton();
        button_drawTriangle = new JButton();
        button_drawRectangle = new JButton();
        button_drawStar = new JButton();
        button_pen.setText("��");
        button_eraser.setText("��Ƥ");
        button_drawLine.setText("ֱ��");
        button_drawTriangle.setText("������");
        button_drawRectangle.setText("����");
        button_drawStar.setText("�����");
        button_drawCircle.setText("��Բ");
        buttonToolGroup.add(button_pen);
        buttonToolGroup.add(button_eraser);
        buttonToolGroup.add(button_drawLine);
        buttonToolGroup.add(button_drawTriangle);
        buttonToolGroup.add(button_drawRectangle);
        buttonToolGroup.add(button_drawStar);
        buttonToolGroup.add(button_drawCircle);

        button_clear = new JButton();
        button_save = new JButton();
        button_clear.setText("���");
        button_save.setText("����");
        //endregion

        int heightOfButtons = 40;//�����и�ֵ���ϵ�Ϊbuttons��panel������Ϊ��������

        //region init frame
        frame.setTitle(title);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);//���ùر�
        frame.setLayout(null);//�ղ���
        frame.setSize(width, height);//����frame��С
        frame.setResizable(false);//���ò��ɵ���
        frame.setLocationRelativeTo(null);//������Ļ������
        frame.add(panel_buttons);
        frame.add(panel_canvas);
        //endregion

        //region init panel_buttons
        panel_buttons.setBounds(0, 0, width, heightOfButtons);
        panel_buttons.add(button_pen);
        panel_buttons.add(button_eraser);
        panel_buttons.add(button_drawLine);
        panel_buttons.add(button_drawCircle);
        panel_buttons.add(button_drawRectangle);
        panel_buttons.add(button_drawTriangle);
        panel_buttons.add(button_drawStar);
        panel_buttons.add(button_clear);
        panel_buttons.add(button_save);
        panel_buttons.setVisible(true);
        //endregion

        //region init panel_canvas
        panel_canvas.setBounds(0, heightOfButtons + 1, width, height - heightOfButtons);
        panel_canvas.setVisible(true);
        panel_canvas.setBackground(Color.white);
        //endregion

        //region init canvas buffer
        canvasBuffer = new BufferedImage(panel_canvas.getWidth(),
                panel_canvas.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics tempBufferImageGraphics = canvasBuffer.createGraphics();
        tempBufferImageGraphics.setColor(panel_canvas.getBackground());//���ñ���ɫ������Ĭ��Ϊȫ��
        tempBufferImageGraphics.fillRect(0, 0, panel_canvas.getWidth(), panel_canvas.getHeight());
        panel_canvas.getGraphics().drawImage(canvasBuffer, 0, 0, null);
        //endregion


    }

    /*
     * ����ť����¼�
     * */
    private void initButtonListeners() {
        button_pen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.PEN;
                setButtonHighlight(button_pen);
            }
        });
        button_eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.ERASER;
                setButtonHighlight(button_eraser);
            }
        });
        button_drawLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.LINE;
                setButtonHighlight(button_drawLine);
            }
        });
        button_drawCircle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.CIRCLE;
                setButtonHighlight(button_drawCircle);
            }
        });
        button_drawTriangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.TRIANGLE;
                setButtonHighlight(button_drawTriangle);
            }
        });
        button_drawRectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.RECTANGLE;
                setButtonHighlight(button_drawRectangle);
            }
        });
        button_drawStar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.STAR;
                setButtonHighlight(button_drawStar);
            }
        });
        button_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBackground(canvasBuffer);
            }
        });
        button_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog(
                        frame,
                        "������ļ���:",
                        ""
                );
                if(fileName != null && !fileName.equals("")){
                    try {
                        ImageIO.write(canvasBuffer, "jpg", new File(fileName + ".jpg"));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    private void setButtonHighlight(JButton button) {
        clearButtonHighlight();
        button.setBackground(Color.yellow);
    }

    private void clearButtonHighlight() {
        for (JButton button : buttonToolGroup) {
            button.setBackground(button_clear.getBackground());
        }
    }

    /*
     * ��ʼ��������¼�����
     * */
    private void initCanvasListeners() {
        //region Mouse pressed & release
        panel_canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startPoint = new Point(e.getX(), e.getY());
                endPoint = startPoint;
                switch (mode) {
                    case NULL:
                        break;
                    case PEN:
                        draw(canvasBuffer);
                        break;
                    case ERASER:
                        draw(canvasBuffer);
                        break;
                    default:
                        tempBufferedImage = copyBufferedImage(canvasBuffer);
                        drawShape(canvasBuffer);
                        break;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                endPoint = new Point(e.getX(), e.getY());
                switch (mode) {
                    case NULL:
                        break;
                    case PEN:
                        break;
                    case ERASER:
                        break;
                    default:
                        canvasBuffer = copyBufferedImage(tempBufferedImage);
                        drawShape(canvasBuffer);
                        break;
                }
                startPoint = null;
                endPoint = null;
            }
        });
        //endregion

        //region Mouse dragged
        panel_canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                endPoint = new Point(e.getX(), e.getY());
                switch (mode) {
                    case NULL:
                        break;
                    case PEN:
                        draw(canvasBuffer);
                        startPoint = endPoint;
                        break;
                    case ERASER:
                        draw(canvasBuffer);
                        startPoint = endPoint;
                        break;
                    default:
                        tempBufferedImage = copyBufferedImage(canvasBuffer);
                        drawShape(tempBufferedImage);
                        break;
                }

            }
        });
        //endregion
    }

    /*
     * ���⻭�������ʺ���Ƥ
     * */
    private void draw(BufferedImage bufferedImage) {
        Graphics bufferedImageGraphics = bufferedImage.getGraphics();
        switch (mode) {
            case PEN:
                bufferedImageGraphics.setColor(lineColor);
                ((Graphics2D) bufferedImageGraphics).setStroke(new BasicStroke(lineThick));
                break;
            case ERASER:
                bufferedImageGraphics.setColor(panel_canvas.getBackground());
                ((Graphics2D) bufferedImageGraphics).setStroke(new BasicStroke(eraserThick));
                break;
        }
        bufferedImageGraphics.drawLine((int) startPoint.getX(),
                (int) startPoint.getY(),
                (int) endPoint.getX(),
                (int) endPoint.getY());
        panel_canvas.getGraphics().drawImage(bufferedImage, 0, 0, null);

    }

    /*
     * ���ݵ�ǰ���ص�mode����������״
     * */
    private void drawShape(BufferedImage bufferedImage) {
        Graphics bufferedImageGraphics = bufferedImage.getGraphics();
        bufferedImageGraphics.setColor(lineColor);
        ((Graphics2D) bufferedImageGraphics).setStroke(new BasicStroke(lineThick));
        switch (mode) {
            case LINE:
                bufferedImageGraphics.drawLine((int) startPoint.getX(),
                        (int) startPoint.getY(),
                        (int) endPoint.getX(),
                        (int) endPoint.getY());
                break;
            case CIRCLE:
                bufferedImageGraphics.drawOval((int) startPoint.getX(),
                        (int) startPoint.getY(),
                        (int) (endPoint.getX() - startPoint.getX()),
                        (int) Math.abs(endPoint.getY() - startPoint.getY()));
                break;
            case RECTANGLE:
                bufferedImageGraphics.drawRect((int) startPoint.getX(),
                        (int) startPoint.getY(),
                        (int) (endPoint.getX() - startPoint.getX()),
                        (int) (endPoint.getY() - startPoint.getY()));
                break;
            case TRIANGLE:
                //��������ĺ�����,˳��Ϊ�׵�1���׵�2������
                //����ΪstartPoint
                int Xs_TRIANGLE[] = {(int) (2 * startPoint.getX() - endPoint.getX()),
                        (int) endPoint.getX(),
                        (int) startPoint.getX()};
                int Ys_TRIANGLE[] = {(int) endPoint.getY(),
                        (int) endPoint.getY(),
                        (int) startPoint.getY()};
                bufferedImageGraphics.drawPolygon(Xs_TRIANGLE, Ys_TRIANGLE, 3);
                break;
            case STAR:
                int Xs_STAR[] = {(int) startPoint.getX(),
                        (int) (startPoint.getX() + (endPoint.getX() - startPoint.getX()) * 5 / 6),
                        (int) (startPoint.getX() + (endPoint.getX() - startPoint.getX()) / 2),
                        (int) (startPoint.getX() + (endPoint.getX() - startPoint.getX()) * 1 / 6),
                        (int) endPoint.getX()
                };
                int Ys_STAR[] = {(int) (startPoint.getY() - (startPoint.getY() - endPoint.getY()) / 3),
                        (int) endPoint.getY(),
                        (int) startPoint.getY(),
                        (int) endPoint.getY(),
                        (int) (startPoint.getY() - (startPoint.getY() - endPoint.getY()) / 3)
                };
                bufferedImageGraphics.drawPolygon(Xs_STAR, Ys_STAR, 5);
                break;
        }
        panel_canvas.getGraphics().drawImage(bufferedImage, 0, 0, null);
    }

    private void drawBackground(BufferedImage bufferedImage) {
        Graphics bufferedImageGraphics = bufferedImage.getGraphics();
        bufferedImageGraphics.setColor(panel_canvas.getBackground());
        bufferedImageGraphics.fillRect(0, 0,
                panel_canvas.getWidth(), panel_canvas.getHeight());
        panel_canvas.getGraphics().drawImage(bufferedImage, 0, 0, null);
    }

    /*
     * ����һ��BufferedImage���ڻ�shapeʱ��
     * ��Ϊ���ɿ����ǰ�ǲ�ȷ���Ƿ��£�������Ҫһ��
     * ��ʱ��BufferedImage����ˢ�¡�
     * */
    private BufferedImage copyBufferedImage(BufferedImage source) {
        if (source != null) {
            //����һ��BufferedImage
            return new BufferedImage(source.getColorModel(),
                    source.copyData(null),
                    source.isAlphaPremultiplied(),
                    null);
        }
        return null;
    }

    /*
     * ö�����ͣ�����ѡ��ǰ�Ļ滭ģʽ
     * */
    public enum Mode {
        PEN("��"),
        ERASER("��Ƥ"),
        LINE("ֱ��"),
        CIRCLE("Բ"),
        TRIANGLE("������"),
        STAR("�����"),
        RECTANGLE("����"),
        NULL("��");
        private final String name;

        private Mode(String name) {
            this.name = name;
        }
    }
}
