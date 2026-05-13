package com.skzh.web.util;

import com.skzh.map.dto.MapPoint;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @ClassName
 * @Description
 * @Author wangq
 * @Date 10:51 2024/11/7
 **/
public class GraphMapUtils {

    /**
     * 根据输入的地点坐标计算中心点
     *
     * @param list
     * @return
     **/
    public static MapPoint getCenterPointFromList(List<MapPoint> list) {
        int total = list.size();
        double X = 0, Y = 0, Z = 0;
        for (MapPoint g : list) {
            double lat, lon, x, y, z;
            lat = g.getLat() * Math.PI / 180;
            lon = g.getLng() * Math.PI / 180;
            x = Math.cos(lat) * Math.cos(lon);
            y = Math.cos(lat) * Math.sin(lon);
            z = Math.sin(lat);
            X += x;
            Y += y;
            Z += z;
        }
        X = X / total;
        Y = Y / total;
        Z = Z / total;
        double Lon = Math.atan2(Y, X);
        double Hyp = Math.sqrt(X * X + Y * Y);
        double Lat = Math.atan2(Z, Hyp);
        return new MapPoint(Lat * 180 / Math.PI, Lon * 180 / Math.PI);
    }

    /**
     * 判断点是否在多边形内
     * <p>
     * 整个算法的思路为：作点平行于y轴的射线，这样就可以直接比较交点是否大于点(x,y)中y的值，可以简化判断。
     *
     * @param point 检测点
     * @param pts   多边形的顶点
     * @return 点在多边形内返回true, 否则返回false
     */
    public static boolean isPtInPoly(Point2D.Double point, List<Point2D.Double> pts) {
        int polygonSidesCount = pts.size();
        //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        boolean boundOrVertex = true;
        // 射线与多边形相交的次数
        int intersectCount = 0;
        //浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;
        // 相邻两点
        Point2D.Double p1, p2;
        // 当前点
        Point2D.Double p = point;
        // 线段起点
        p1 = pts.get(0);
        for (int i = 1; i <= polygonSidesCount; ++i) {
            //按照点录入的顺序依次检查相邻两点组成的线段和当前的点的关系。
            if (p.equals(p1)) {
                //如果当前点就是多边形的顶点之一，直接返回。
                return boundOrVertex;
            }
            // 线段终点
            p2 = pts.get(i % polygonSidesCount);
            if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {
                //点在x轴上的映射，明显超出了线段在x轴上的投影。此时冲着上做射线，肯定没有交点，直接跳过。
                p1 = p2;
                continue;
            }
            if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {
                // 当前点在线段于x轴上的投影内
                if (p.y <= Math.max(p1.y, p2.y)) {
                    //当前点的 y 坐标小于 线段对y轴投影的最大值
                    if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {
                        // 若线段同样是平行于y轴，则可以断定，当前点，在多边形的这条垂直于x轴的边上。
                        return boundOrVertex;
                    }
                    if (p1.y == p2.y) {
                        // 若线段为平行于x轴的水平线
                        if (p1.y == p.y) {
                            //当前点正好位于该水平线上，直接返回该点位于多边形的一条边上。
                            return boundOrVertex;
                        } else {
                            //如果当前点在水平线以下，增加一个交点。
                            ++intersectCount;
                        }
                    } else {
                        // 如果不是水平线，则用两点式求当前点的x带入多边形线段的直线方程后，对应的y的坐标
                        double xInSideLineFormulaResultY = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        if (Math.abs(p.y - xInSideLineFormulaResultY) < precision) {
                            // 误差允许范围内，改点就在线段上，则表明，该点位于多边形的一个边上。
                            return boundOrVertex;
                        }

                        if (p.y < xInSideLineFormulaResultY) {
                            // 如果线段上取得的y比当前点的y要大，当前做向上的射线，肯定交于上方的一个点。
                            ++intersectCount;
                        }
                    }
                }
            } else {
                // 当前点不在线段投影到x轴的区间中
                if (p.x == p2.x && p.y <= p2.y) {
                    // 但恰好位于线段终点的x坐标对应的平行于y轴上的线上的低于终点y的一点
                    // 此时检查下一点能否将其x边界包含。
                    Point2D.Double p3 = pts.get((i + 1) % polygonSidesCount);
                    if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {
                        // 若当前点的x坐标位于 p1和p3组成的线段关于x轴的投影中，则记为该点的射线只穿过端点一次。
                        ++intersectCount;
                    } else {
                        // 若当前点的x坐标不能包含在p1和p3组成的线段关于x轴的投影中，则点射线通过的两条线段组成了一个弯折的部分，
                        // 此时我们记射线穿过该端点两次
                        intersectCount += 2;
                    }
                    // 此判断的核心思路是由点在两条线段的内部还是外部去思考得出的
                }
            }
            // 进行下一个线段的判断
            p1 = p2;
        }
        if (intersectCount % 2 == 0) {
            //偶数在多边形外
            return false;
        } else {
            //奇数在多边形内
            return true;
        }
    }

}
