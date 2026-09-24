package com.blog.smoke;

import com.blog.common.BaseTest;
import com.blog.common.UiHelper;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SmokeTest extends BaseTest {

    @Test
    public void test01打开登录页() {
        driver.get(BASE_URL + "/blog_login.html");
        Assert.assertTrue("未打开登录页，当前URL: " + driver.getCurrentUrl(),
                driver.getCurrentUrl().contains("blog_login.html"));
        System.out.println("冒烟通过：登录页打开成功");
    }

    @Test
    public void test02登录成功跳列表页() {
        UiHelper.login(driver, "zhangsan", "123456");
        // 列表页至少出现一条博客（.detail 是"查看全文>>"链接）
        List<WebElement> blogs = driver.findElements(By.cssSelector(".container .right .detail"));
        Assert.assertTrue("列表页没有博客数据", blogs.size() > 0);
        System.out.println("冒烟通过：登录成功，列表显示 " + blogs.size() + " 条博客");
    }

    @Test
    public void test03查看详情页() {
        UiHelper.login(driver, "zhangsan", "123456");
        driver.findElement(By.cssSelector(".container .right .detail")).click();
        Assert.assertTrue("未跳转详情页，当前URL: " + driver.getCurrentUrl(),
                driver.getCurrentUrl().contains("blog_detail.html?blogId="));
        System.out.println("冒烟通过：详情页打开成功");
    }

    @Test
    public void test04新增博客() {
        String title = "冒烟-新增" + System.currentTimeMillis();
        UiHelper.login(driver, "zhangsan", "123456");
        driver.get(BASE_URL + "/blog_edit.html");
        driver.findElement(By.id("title")).sendKeys(title);
        // content 是隐藏 textarea，用 jQuery 直接赋值
        ((JavascriptExecutor) driver).executeScript("$('#content').val('冒烟测试内容')");
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept(); // "发表成功！"
        // 跳回列表页后，新标题应出现
        Assert.assertTrue("列表中未找到新博客：" + title,
                driver.getPageSource().contains(title));
        System.out.println("冒烟通过：新增博客成功，标题=" + title);
    }

    @Test
    public void test05编辑博客() {
        String oldTitle = "冒烟-编辑" + System.currentTimeMillis();
        String newTitle = oldTitle + "-改";
        // 先新增一条
        UiHelper.login(driver, "zhangsan", "123456");
        driver.get(BASE_URL + "/blog_edit.html");
        driver.findElement(By.id("title")).sendKeys(oldTitle);
        ((JavascriptExecutor) driver).executeScript("$('#content').val('编辑前内容')");
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept();

        // 列表页找到这条博客，点"查看全文"进详情，再点"编辑"
        driver.get(BASE_URL + "/blog_list.html");
        driver.findElement(By.xpath("//div[contains(text(),'" + oldTitle + "')]/following::a[contains(text(),'查看全文')]")).click();
        driver.findElement(By.xpath("//button[contains(text(),'编辑')]")).click();
        // 编辑页：清空标题重填，保存
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.clear();
        titleInput.sendKeys(newTitle);
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept(); // "修改成功"
        Assert.assertTrue("列表未显示修改后的标题：" + newTitle,
                driver.getPageSource().contains(newTitle));
        System.out.println("冒烟通过：编辑博客成功");
    }

    @Test
    public void test06删除博客() {
        String title = "冒烟-删除" + System.currentTimeMillis();
        // 先新增一条
        UiHelper.login(driver, "zhangsan", "123456");
        driver.get(BASE_URL + "/blog_edit.html");
        driver.findElement(By.id("title")).sendKeys(title);
        ((JavascriptExecutor) driver).executeScript("$('#content').val('删除前内容')");
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept();

        // 列表页进详情，点删除
        driver.get(BASE_URL + "/blog_list.html");
        driver.findElement(By.xpath("//div[contains(text(),'" + title + "')]/following::a[contains(text(),'查看全文')]")).click();
        driver.findElement(By.xpath("//button[contains(text(),'删除')]")).click();
        UiHelper.waitAlert(driver).accept(); // "删除成功！"
        // 回列表页，标题应消失
        Assert.assertFalse("删除后列表仍显示该博客：" + title,
                driver.getPageSource().contains(title));
        System.out.println("冒烟通过：删除博客成功");
    }
}
