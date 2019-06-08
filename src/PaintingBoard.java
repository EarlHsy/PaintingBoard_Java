
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
    public final static String title = "Hsy's PaintingBoard";//标题
    private int width = 1024;//窗口宽度
    private int height = 768;//窗口高度
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
    private Point startPoint;//记录画图起始点
    private Point endPoint;//画图结束点
    private Mode mode;//当前选择绘画模式
    private int lineThick = 5;//线条粗细直径
    private Color lineColor = Color.red;//线条颜色
    private int eraserThick = 50;//橡皮粗细直径
    //endregion

    public PaintingBoard() {
        initUI();
        initCanvasListeners();
        initButtonListeners();
    }

    /*
     * 初始化UI，包括JFrame、Button、JPanel等
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
        button_pen.setText("笔");
        button_eraser.setText("橡皮");
        button_drawLine.setText("直线");
        button_drawTriangle.setText("三角形");
        button_drawRectangle.setText("矩形");
        button_drawStar.setText("五角星");
        button_drawCircle.setText("椭圆");
        buttonToolGroup.add(button_pen);
        buttonToolGroup.add(button_eraser);
        buttonToolGroup.add(button_drawLine);
        buttonToolGroup.add(button_drawTriangle);
        buttonToolGroup.add(button_drawRectangle);
        buttonToolGroup.add(button_drawStar);
        buttonToolGroup.add(button_drawCircle);

        button_clear = new JButton();
        button_save = new JButton();
        button_clear.setText("清空");
        button_save.setText("保存");
        //endregion

        int heightOfButtons = 40;//窗口中该值以上的为buttons的panel，以下为画板区域

        //region init frame
        frame.setTitle(title);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);//设置关闭
        frame.setLayout(null);//空布局
        frame.setSize(width, height);//设置frame大小
        frame.setResizable(false);//设置不可调整
        frame.setLocationRelativeTo(null);//设置屏幕正中央
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
        tempBufferImageGraphics.setColor(panel_canvas.getBackground());//设置背景色，否则默认为全黑
        tempBufferImageGraphics.fillRect(0, 0, panel_canvas.getWidth(), panel_canvas.getHeight());
        panel_canvas.getGraphics().drawImage(canvasBuffer, 0, 0, null);
        //endregion


    }

    /*
     * 给按钮添加事件
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
                        "保存的文件名:",
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
     * 初始化画板的事件监听
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
     * 随意画，包括笔和橡皮
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
     * 根据当前记载的mode类型来画形状
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
                //三个顶点的横坐标,顺序为底点1，底点2，顶点
                //顶点为startPoint
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
     * 拷贝一个BufferedImage。在画shape时，
     * 因为在松开鼠标前是不确定是否画下，所以需要一个
     * 临时的BufferedImage不断刷新。
     * */
    private BufferedImage copyBufferedImage(BufferedImage source) {
        if (source != null) {
            //拷贝一个BufferedImage
            return new BufferedImage(source.getColorModel(),
                    source.copyData(null),
                    source.isAlphaPremultiplied(),
                    null);
        }
        return null;
    }

    /*
     * 枚举类型，用于选择当前的绘画模式
     * */
    public enum Mode {
        PEN("笔"),
        ERASER("橡皮"),
        LINE("直线"),
        CIRCLE("圆"),
        TRIANGLE("三角形"),
        STAR("五角星"),
        RECTANGLE("矩形"),
        NULL("无");
        private final String name;

        private Mode(String name) {
            this.name = name;
        }
    }
}
