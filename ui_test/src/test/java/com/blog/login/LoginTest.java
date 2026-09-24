package com.blog.login;

import com.blog.common.BaseTest;
import com.blog.common.UiHelper;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

public class LoginTest extends BaseTest {

    @Test
    public void test01登录成功跳转列表页() {
        UiHelper.login(driver, "zhangsan", "123456");
        Assert.assertTrue("登录后未跳转列表页",
                driver.getCurrentUrl().contains("blog_list.html"));
        System.out.println("通过：登录成功跳转列表页");
    }

    @Test
    public void test02密码错误提示() {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("username")).sendKeys("zhangsan");
        driver.findElement(By.id("password")).sendKeys("111111");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("密码错误"));
        alert.accept();
        // 仍停留在登录页
        Assert.assertTrue(driver.getCurrentUrl().contains("blog_login.html"));
        System.out.println("通过：密码错误弹窗提示");
    }

    @Test
    public void test03用户名不存在提示() {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("username")).sendKeys("not_exist");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("用户不存在"));
        alert.accept();
        System.out.println("通过：用户名不存在弹窗提示");
    }

    @Test
    public void test04用户名为空提示() {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("用户名长度必须在3-20之间"));
        alert.accept();
        System.out.println("通过：用户名为空弹长度提示");
    }

    @Test
    public void test05密码为空提示() {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("username")).sendKeys("zhangsan");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("密码长度必须在6-20之间"));
        alert.accept();
        System.out.println("通过：密码为空弹长度提示");
    }

    @Test
    public void test06用户名2位提示() {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("username")).sendKeys("ab");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("用户名长度必须在3-20之间"));
        alert.accept();
        System.out.println("通过：用户名2位弹长度提示");
    }

    @Test
    public void test07密码5位提示() {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("username")).sendKeys("zhangsan");
        driver.findElement(By.id("password")).sendKeys("12345");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("密码长度必须在6-20之间"));
        alert.accept();
        System.out.println("通过：密码5位弹长度提示");
    }
}
