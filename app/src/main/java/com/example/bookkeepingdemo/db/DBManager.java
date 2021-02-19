package com.example.bookkeepingdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.github.mikephil.charting.charts.Chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;

    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();

    }

    /***
     * 读取数据库当中的数据
     * 写入内存集合里
     */
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind1);
            list.add(typeBean);


        }
        return list;
    }

    // 向记账表当中插入一条匀速
    public static void insertToAccounttb(AccountBean bean){

        ContentValues values = new ContentValues();
        values.put("typename", bean.getTypename());
        values.put("sImageId", bean.getsImageId());
        values.put("beizhu", bean.getBeizhu());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());
        db.insert("accounttb",null, values);
        Log.i("test_demo", "insert to accounttb test: ok!!!!!");
    }

    /***
     * 获取记账表当中某一天所有支出和收入情况
     */
    public static List<AccountBean> getAccountListOneDayFromAccounttb(int year, int month, int day){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));

            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;

    }

    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year, int month){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;

    }

    /***
     * 获取某一天的支出或者收入的总金额 kind: 支出0 收入1
     */
    public static float getSumMoneyOneDay(int year, int month, int day, int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    public static float getSumMoneyOneMonth(int year, int month, int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    public static float getSumMoneyOneYear(int year, int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    public static int getCountItemOneMonth(int year, int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    // 根据传入id删除accounttb中的一条数据
    public  static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    // 根据备注搜索收入或者支出的情况列表
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb from where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String beizhu2 = cursor.getString(cursor.getColumnIndex("beizhu"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));

            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu2, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    public static List<Integer>getYearListByYearFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    // 删除accounttb中所有数据
    public static void  deleteAllAccount(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
        Log.i("test", "deleteAllAccount: ok!!!");
    }

    // 查询指定年份和月份的收入或支出的总钱数
    public static List<ChartItemBean>getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year,month,kind);
        String sql = "select typename, sImageId, sum(money)as total from accounttb where year=? and month=? and kind=?" +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));

            float ratio = div(total,sumMoneyOneMonth);
            ChartItemBean chartItemBean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(chartItemBean);

        }
        return list;
    }

    public static float getMaxMoneyOneDayInOneMonth(int year,int month, int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year+"", month+"", kind+""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    public static List<BarChartBean> getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartBean bean = new BarChartBean(year,month,day,money);
            list.add(bean);
        }
        return list;
    }

    public static float div(float v1, float v2){
        float v3 = v1/v2;
        BigDecimal b1 = new BigDecimal(v3);
        float val = b1.setScale(4,4).floatValue();
        return  val;

    }
}
