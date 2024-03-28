<template>
  <div class="todo-header">
    <!-- 当按下回车后会执行add回调函数 -->
    <input
      type="text"
      placeholder="请输入你的任务名称，按回车键确认"
      v-model="title"
      @keyup.enter="add"
    />
  </div>
</template>

<script>
// 引入nanoid库,他使用了分别暴露的形式
// {nanoid} 是一个函数，我们直接调用就可以给我们一个唯一id
import { nanoid } from "nanoid";

export default {
  name: "MyHeader",
  data() {
    return {
      title: "",
    };
  },
  // 父组件APP传递过来的函数receive
  props: ["receive"],
  methods: {
    add(event) {
      console.log(this.title);
      // 下面这样也能获取到文本框中的内容
      // console.log(event.target.value)

      // 将用户的输入包装秤一个todo对象
      // id可以使用uuid，或者nanoid
      const todoObj = {
        id: nanoid(),
        title: this.title,
        done: false,
      };
      console.log(todoObj);
      this.receive(todoObj);
      //清空文本框
      this.title = "";
    },
  },
};
</script>

<style  scoped>
/*header*/
.todo-header input {
  width: 560px;
  height: 28px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 4px 7px;
}

.todo-header input:focus {
  outline: none;
  border-color: rgba(82, 168, 236, 0.8);
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
    0 0 8px rgba(82, 168, 236, 0.6);
}
</style>