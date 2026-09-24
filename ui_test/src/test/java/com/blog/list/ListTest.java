package com.blog.list;

import com.blog.common.BaseTest;
import com.blog.common.UiHelper;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ListTest extends BaseTest {

    @Test
    public void test01列表展示博客数据() {
        String title = "列表-" + System.currentTimeMillis();
        UiHelper.login(driver, "zhangsan", "123456");
        // 先新增一条
        driver.get(BASE_URL + "/blog_edit.html");
        driver.findElement(By.id("title")).sendKeys(title);
        ((JavascriptExecutor) driver).executeScript("$('#content').val('列表测试内容')");
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept();
        // 回列表页，断言新博客出现
        Assert.assertTrue("列表未显示新增的博客：" + title,
                driver.getPageSource().contains(title));
        List<WebElement> blogs = driver.findElements(By.cssSelector(".container .right .blog"));
        Assert.assertTrue("列表没有博客", blogs.size() > 0);
        System.out.println("通过：列表显示 " + blogs.size() + " 条博客");
    }

    @Test
    public void test02查看全文跳转详情页() {
        UiHelper.login(driver, "zhangsan", "123456");
        driver.findElement(By.cssSelector(".container .right .detail")).click();
        Assert.assertTrue("未跳转详情页，当前URL: " + driver.getCurrentUrl(),
                driver.getCurrentUrl().contains("blog_detail.html?blogId="));
        // 详情页标题元素有内容
        WebElement title = driver.findElement(By.cssSelector(".content .title"));
        Assert.assertTrue("详情页标题为空", title.getText().length() > 0);
        System.out.println("通过：查看全文跳转详情页，标题=" + title.getText());
    }

    @Test
    public void test03注销清除token() {
        UiHelper.login(driver, "zhangsan", "123456");
        driver.findElement(By.xpath("//a[contains(text(),'注销')]")).click();
        Assert.assertTrue("注销后未跳回登录页",
                driver.getCurrentUrl().contains("blog_login.html"));
        // localStorage 里的 token 应被清除
        Object token = ((JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('token')");
        Assert.assertNull("注销后 token 未被清除，实际：" + token, token);
        System.out.println("通过：注销跳回登录页且 token 已清除");
    }
}
