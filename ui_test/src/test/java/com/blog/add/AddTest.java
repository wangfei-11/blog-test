package com.blog.add;

import com.blog.common.BaseTest;
import com.blog.common.UiHelper;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class AddTest extends BaseTest {

    // 打开写博客页（需先登录）
    private void openEditPage() {
        UiHelper.login(driver, "zhangsan", "123456");
        driver.get(BASE_URL + "/blog_edit.html");
    }

    @Test
    public void test01新增博客成功() {
        String title = "新增-" + System.currentTimeMillis();
        openEditPage();
        driver.findElement(By.id("title")).sendKeys(title);
        ((JavascriptExecutor) driver).executeScript("$('#content').val('新增测试内容')");
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept(); // "发表成功！"
        // 跳回列表页且新博客出现
        Assert.assertTrue("发表后未回列表页",
                driver.getCurrentUrl().contains("blog_list.html"));
        Assert.assertTrue("列表未显示新博客：" + title,
                driver.getPageSource().contains(title));
        System.out.println("通过：新增博客成功，标题=" + title);
    }

    @Test
    public void test02标题为空拦截() {
        openEditPage();
        // 不填标题，直接点发布
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("标题不能为空"));
        alert.accept();
        // 仍停留在写博客页，且没有产生数据
        Assert.assertTrue("被拦截后应停留在编辑页",
                driver.getCurrentUrl().contains("blog_edit.html"));
        System.out.println("通过：标题为空被拦截");
    }

    @Test
    public void test03内容为空拦截() {
        openEditPage();
        driver.findElement(By.id("title")).sendKeys("内容为空-" + System.currentTimeMillis());
        // 内容框有默认值"##在这里写下一篇博客"，先清空
        ((JavascriptExecutor) driver).executeScript("$('#content').val('')");
        driver.findElement(By.id("submit")).click();
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("博客内容不能为空"));
        alert.accept();
        System.out.println("通过：内容为空被拦截");
    }
}
