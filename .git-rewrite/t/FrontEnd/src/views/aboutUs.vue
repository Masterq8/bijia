<template>

  <div class="app-content" style="display: flex; justify-content: center;">
    <div class style="display: flex; width: 77vw; flex-direction: column;">
      <div class="app-content-co">
        <div class="content-left">
          <div class="left-item">
            <div class="item-title">
              <div class="item-title-text">平台介绍</div>
            </div>
            <div class="item-content">
              <div class="item-content-textbox">
                <p style="text-indent:2em;line-height:25px;letter-spacing:1px;">
                  海洋海岸带遥感影像智能解译云平台是基于深度学习技术的智能化遥感解译系统，专注于海洋与海岸带环境的动态监测与分析。平台通过AI技术实现自动化遥感影像处理，可精准识别海岸线变迁、海水养殖区、滨海湿地、海洋生态灾害等关键要素，支持高频次动态监测，为资源管理、生态保护等提供决策支持。采用"数据-算法-模型-算力"一体化架构，集成从影像预处理到专题图生成的全流程功能，显著提升解译效率。
                </p>
                <p style="text-indent:2em;line-height:25px;letter-spacing:1px;">
                  平台核心技术包括海洋遥感大模型预训练与微调技术、端到端专题产品生成技术，支持自然语言交互，大幅降低使用门槛。目前已在国家海洋环境监测中心、山东省海洋资源与环境研究院等单位初步应用，为自然资源管理、海洋生态监测等业务提供智能化解决方案。
                </p>
              </div>

              <div style="float:right; clear: both;margin-left: 15px;">
                <img src="@/assets/images/xx.png" style="width: 220px; height: auto; margin-left: 20px; margin-top: 30px;" alt />
              </div>


            </div>
          </div>
          <div class="left-item">
            <div class="item-title">

              <div class="item-title-text">实验室介绍</div>
            </div>
            <div class="item-content">

              <div class="item-content-textbox">
                <p style="text-indent:2em;line-height:25px;letter-spacing:1px;"> 智能技术与遥感实验室（ITRS
                  Lab）隶属于山东科技大学计算机科学与工程学院。实验室负责人为崔宾阁教授，现有教师5人，在校研究生30余人，承担了国家自然科学基金项目2项，国家重点研发计划项目子课题1项，山东省自然科学基金项目4项，企事业单位技术委托项目等科研项目10多项，发表学术论文50余篇，其中SCI收录论文20篇以上，已授权发明专利5项。
                </p>

              </div>



            </div>
          </div>
        </div>

        <div class="content-right">
          <div class="right-content">
            <div>
              <div class="right-title">
                <b>联系我们</b>
              </div>
              <div class="right-first-conent">
                <b>邮箱：</b>
                <br />
                <a href="/cdn-cgi/l/email-protection" class="__cf_email__"
                  data-cfemail="571e0305041b3635176661647934383a">ITRSLab@163.com</a>
                <!-- [email&#160;protected] -->
              </div>
              <div class="right-first-conent">
                <b>联系电话：</b>
                <br />崔老师 13969680498
                <br />于老师 18866207479
                <br />邢老师 15376768608
                <br />
              </div>
              <div style="margin-top: 24px;">
                <div style="display: flex;">
                  <img src="/resData/qq_icon.png" style="height: 20px;
                                        width: 20px;" />
                  <span class="right-font">
                    QQ群
                    <br />514454292
                  </span>
                </div>
                <!--                            <img src="/resData/qq_group.png" class="right-img">-->
                <img src="/resData/qqgroup.jpg" class="right-img" />
              </div>

              <div style="margin-top: 24px;">
                <div style="display: flex;">
                  <img src="/resData/weixin_icon.png" style="height: 20px;
                                        width: 20px;" />
                  <span class="right-font">
                    微信公众号
                    <br />ITRS Lab
                  </span>
                </div>
                <img src="/resData/640.jpg" class="right-img" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- <br/> <img src="../assets/images/xx.jpg" style="width: 100%;" /> -->
  </div>
</template>

<script setup name="aboutUs">
// import xxImage from '/assets/images/xx.jpg';



</script>

<style scoped lang="scss">
.content-left {
  width: 75%;
  display: flex;
  flex-direction: column;
}

.content-right {
  width: 25%;
  /* background-image: linear-gradient(163deg, rgba(179, 210, 255, .06), rgba(199, 180, 255, .08)); */
  border-radius: 4px;
  margin-left: 20px;
  padding: 30px;
  margin-bottom: 10px;

}

.left-item {
  // background-image: linear-gradient(127deg,
  //     rgba(179, 210, 255, 0.06),
  //     rgba(199, 180, 255, 0.08));
  // border-radius: 4px;
  background: #1D1D1F;
  border-radius: 8px;
  flex: 1;
  padding: 20px 24px 24px;
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.item-title {
  height: 36px;
  background: #00AEEF;
  box-shadow: 0px 2px 6px 0px rgba(127, 220, 255, 0.64);
  border-radius: 5px;
  text-align: center;

  .item-title-text {
    font-family: Source Han Sans CN;
    font-weight: 500;
    font-size: 15px;
    color: #FFFFFF;
    line-height: 36px;
    text-shadow: 0px 1px 4px rgba(65, 64, 64, 0.74);
  }
}

.item-content {
  // font-size: 14px;
  // font-weight: 400;
  // letter-spacing: 0;
  // line-height: 22px;
  // text-align: justify;
  // padding: 20px 20px;
  display: flex;
  flex-direction: row;
  margin-top: 30px;
  padding: 10px 20px 10px 20px;
  // background: #696969;
  background:rgba(105, 105, 105, 0.64) ;
  box-shadow: 0px 2px 29px 0px rgba(105, 105, 105, 0.64);

  border: 1px solid #919191;
// border-image: linear-gradient(159deg, rgba(255,255,255,0.72), rgba(255,255,255,0.72)) 1 1;
  min-height:280px;
  border-radius: 20px;
  .item-content-textbox {

    font-family: 'Source Han Sans CN';
    font-weight: 400;
    font-size: 14px;
    color: #FFFFFF;
    line-height: 30px;
  }


}

.right-content {
  /* background-image: linear-gradient(163deg, rgba(179, 210, 255, .06), rgba(199, 180, 255, .08)); */
  border-radius: 4px;
  margin-left: 20px;
  /* padding: 44px ; */
}

.right-title {
  color: #e8eaed;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 0;
  line-height: 19px;
  text-align: justify;
}

.right-first-conent {
  color: rgba(232, 234, 237, 0.8);
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: 19px;
  margin-top: 24px;
}

.right-font {
  color: rgba(232, 234, 237, 0.8);
  display: block;
  font-size: 15px;
  line-height: 20px;
  margin-left: 4px;
}

.right-img {
  border-radius: 2px;
  height: 152px;
  margin-top: 8px;
  width: 152px;
}

.app-content {
  background: #17181a;
  //   height: calc(100% - 55px);
  //height: 100%;
  height: calc(100vh - 50px);
  top: 50px;
  /*width: 100%;*/
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.w {
  width: 1226px;
  margin: 0 auto;
}

a {
  text-decoration: none;
  color: #fff;
}

img {
  border-style: none;
  vertical-align: middle;
}

.app-content-co {
  height: 100%;
  overflow: hidden auto;
  margin: 40px 60px;
  display: flex;
}
</style>
