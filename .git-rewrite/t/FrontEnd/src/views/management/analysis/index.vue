<template>
  <div>
    <div ref="myChart" id="myChart" style="width: 100%; height: 450px;"></div>
    <div ref="myChart2" id="myChart2" style="width: 100%; height: 450px;"></div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts';
import { listMain } from "@/api/order/orderInfo";
import { ref, onMounted } from 'vue';
import { getDailyVisits } from '@/api/visits/visits';

const myChart = ref(null);
const myChart2 = ref(null);

onMounted(async () => {
  const chart = echarts.init(myChart.value);
  const chart2 = echarts.init(myChart2.value);

  try {
    // 调用接口获取数据
    const response = await listMain();
    const data = response.data;
     const response2 = await getDailyVisits();
    const data2 = response2.data; // 假设接口返回的数据是一个数组，每个元素都是一个对象
console.log(data2);

    // 转换数据为ECharts所需的格式
    const seriesData = data.map(item => item.number); // 假设每个对象都有一个名为yAxisValue的属性
    const xAxisData = data.map(item => item.satelliteType); // 假设每个对象都有一个名为xAxisValue的属性
    const seriesData2 = data2.map(item => item.count); // 假设每个对象都有一个名为yAxisValue的属性
    const xAxisData2 = data2.map(item => item.date); // 假设每个对象都有一个名为xAxisValue的属性

    // 准备图表配置
    const option = {
      title: { text: '数据类型' },
      // tooltip: {},
      tooltip: {
          trigger: 'axis',
          axisPointer: {
              type: 'shadow'
          }
      },
      xAxis: {name:'类型', data: xAxisData },
      yAxis: {name:'数量'},
      series: [
        {
          name: '数量',
          type: 'bar',
          data: seriesData,
        },
      ],
    };


    var option2 = {
        title: {
            text: '网站访问量'
        },
        tooltip: {},
        xAxis: {
          name: '日期',
            type: 'category',
            boundaryGap: false,
            data: xAxisData2 // 动态填充
        },
        yAxis: {
          name: '访问量',
            type: 'value'
        },
        series: [{
            name: '访问量',
            type: 'line',
            data: seriesData2 // 动态填充
        }]
    };


    // 设置图表配置
    chart.setOption(option);
    chart2.setOption(option2);

    window.addEventListener('resize', () => chart.resize());
  } catch (error) {
    console.error('Error fetching data:', error);
  }
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', () => myChart.value.resize());
});
</script>
