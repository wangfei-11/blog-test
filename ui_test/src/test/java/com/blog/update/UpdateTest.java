package com.blog.update;

import com.blog.common.BaseTest;
import com.blog.common.UiHelper;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UpdateTest extends BaseTest {

    // 新增一条博客并返回它的编辑页 URL
    private String createBlogAndGetUpdateUrl(String title, String content) {
        UiHelper.login(driver, "zhangsan", "123456");
        driver.get(BASE_URL + "/blog_edit.html");
        driver.findElement(By.id("title")).sendKeys(title);
        ((JavascriptExecutor) driver).executeScript("$('#content').val(arguments[0])", content);
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept();
        // 从列表页找"查看全文"，拼出编辑页 URL（blogId 从 href 里截）
        String xpath = "//div[contains(@class,'blog')][.//div[contains(@class,'title') and contains(text(),'"
                + title + "')]]//a[contains(text(),'查看全文')]";
        String href = driver.findElement(By.xpath(xpath)).getAttribute("href");
        String blogId = href.substring(href.indexOf("blogId=") + "blogId=".length());
        return BASE_URL + "/blog_update.html?blogId=" + blogId;
    }

    @Test
    public void test01修改博客成功() {
        String title = "更新-" + System.currentTimeMillis();
        String newTitle = title + "-改";
        String url = createBlogAndGetUpdateUrl(title, "更新前内容");
        driver.get(url);
        // 等回显完成，改标题
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.clear();
        titleInput.sendKeys(newTitle);
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept(); // "修改成功"
        // 回列表页，新标题出现
        Assert.assertTrue("修改后未回列表页",
                driver.getCurrentUrl().contains("blog_list.html"));
        Assert.assertTrue("列表未显示修改后的标题：" + newTitle,
                driver.getPageSource().contains(newTitle));
        System.out.println("通过：修改博客成功，新标题=" + newTitle);
    }

    @Test
    public void test02编辑页回显正确() {
        String title = "回显-" + System.currentTimeMillis();
        String content = "回显测试内容abc123";
        String url = createBlogAndGetUpdateUrl(title, content);
        driver.get(url);
        // 等编辑器加载完、内容回显进 CodeMirror（最多 5 秒）
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.cssSelector(".CodeMirror"), content));
        // 标题回显
        String titleVal = driver.findElement(By.id("title")).getAttribute("value");
        Assert.assertEquals("标题回显不对", title, titleVal);
        System.out.println("通过：编辑页标题/内容回显正确");
    }


    @Test
    public void test03越权修改被拒绝() {
        String title = "越权-" + System.currentTimeMillis();
        String url = createBlogAndGetUpdateUrl(title, "越权测试内容");
        // 注销 zhangsan，换 lisi 登录，直接打开 zhangsan 博客的编辑页
        driver.findElement(By.xpath("//a[contains(text(),'注销')]")).click();
        UiHelper.login(driver, "lisi", "123456");
        driver.get(url);
        // 改标题并保存
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.clear();
        titleInput.sendKeys("越权篡改");
        driver.findElement(By.id("submit")).click();
        // 后端拒绝，弹"无权限"
        Alert alert = UiHelper.waitAlert(driver);
        Assert.assertTrue("提示语不对，实际：" + alert.getText(),
                alert.getText().contains("无权限"));
        alert.accept();
        System.out.println("通过：越权修改被后端拒绝");
    }
}
