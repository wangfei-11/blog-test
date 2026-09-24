package com.blog.common;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UiHelper {

    public static final String BASE_URL = "http://localhost:8081";  // 注意：blog 端口是 8081

    // 等 alert 弹窗（最多 5 秒）
    public static Alert waitAlert(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    // 登录：成功直接跳 blog_list.html（blog 登录成功不弹窗，只有失败才弹 alert）
    public static void login(WebDriver driver, String username, String password) {
        driver.get(BASE_URL + "/blog_login.html");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("submit")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("blog_list.html"));
    }
}
