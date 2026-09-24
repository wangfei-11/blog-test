package com.blog.detail;

import com.blog.common.BaseTest;
import com.blog.common.UiHelper;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class DetailTest extends BaseTest {

    // 新增一条博客并返回它的详情页 URL
    private String createBlogAndGetDetailUrl(String title) {
        UiHelper.login(driver, "zhangsan", "123456");
        driver.get(BASE_URL + "/blog_edit.html");
        driver.findElement(By.id("title")).sendKeys(title);
        ((JavascriptExecutor) driver).executeScript("$('#content').val('详情测试内容')");
        driver.findElement(By.id("submit")).click();
        UiHelper.waitAlert(driver).accept();
        // 从列表页拿"查看全文"的 href
        String xpath = "//div[contains(@class,'blog')][.//div[contains(@class,'title') and contains(text(),'"
                + title + "')]]//a[contains(text(),'查看全文')]";
        return driver.findElement(By.xpath(xpath)).getAttribute("href");
    }

    @Test
    public void test01详情页内容显示正确() {
        String title = "详情-" + System.currentTimeMillis();
        String url = createBlogAndGetDetailUrl(title);
        driver.get(url);
        // 标题显示正确
        WebElement titleEl = driver.findElement(By.cssSelector(".content .title"));
        Assert.assertEquals("详情页标题不对", title, titleEl.getText());
        // markdown 内容渲染出来了（#detail 区域有内容）
        Assert.assertTrue("详情页内容未渲染",
                driver.findElement(By.id("detail")).getText().length() > 0);
        System.out.println("通过：详情页标题/内容显示正确");
    }

    @Test
    public void test02非作者看不到编辑删除按钮() {
        String title = "权限-" + System.currentTimeMillis();
        String url = createBlogAndGetDetailUrl(title);
        // 注销 zhangsan，换 lisi 登录（博客作者是 zhangsan）
        driver.findElement(By.xpath("//a[contains(text(),'注销')]")).click();
        UiHelper.login(driver, "lisi", "123456");
        driver.get(url);
        // 等详情加载
        Assert.assertTrue("详情页标题为空",
                driver.findElement(By.cssSelector(".content .title")).getText().length() > 0);
        // 编辑/删除按钮不应出现
        int editBtns = driver.findElements(By.xpath("//button[contains(text(),'编辑')]")).size();
        int deleteBtns = driver.findElements(By.xpath("//button[contains(text(),'删除')]")).size();
        Assert.assertEquals("非作者不应看到编辑按钮，实际：" + editBtns, 0, editBtns);
        Assert.assertEquals("非作者不应看到删除按钮，实际：" + deleteBtns, 0, deleteBtns);
        System.out.println("通过：非作者无编辑/删除按钮");
    }

    @Test
    public void test03作者删除博客成功() {
        String title = "删除-" + System.currentTimeMillis();
        String url = createBlogAndGetDetailUrl(title);
        driver.get(url);
        // 等删除按钮出现（作者可见）
        WebElement deleteBtn = driver.findElement(By.xpath("//button[contains(text(),'删除')]"));
        deleteBtn.click();
        UiHelper.waitAlert(driver).accept(); // "删除成功！"
        // 跳回列表页，标题消失
        Assert.assertTrue("删除后未回列表页",
                driver.getCurrentUrl().contains("blog_list.html"));
        Assert.assertFalse("删除后列表仍显示该博客：" + title,
                driver.getPageSource().contains(title));
        System.out.println("通过：作者删除博客成功");
    }
}
