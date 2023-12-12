export default {
    install(Vue) {
        //1. 全局过滤器
        Vue.filter('mySlice', function (value) {
            return value.slice(0, 4)
        })

        //2. 全局自定义指令
        Vue.directive('fbind', {
            //指令与元素成功绑定时（初始时）
            bind(element, binding) {
                element.value = binding.value
            },
            //指令所在元素被插入页面时
            inserted(element, binding) {
                element.focus()
            },
            //指令所在的模板被重新解析时
            update(element, binding) {
                element.value = binding.value
            },

        })

        //3. 定义混入
        Vue.mixin({
            data() {
                return {
                    x: 100,
                    y: 200
                }
            }
        })
        //4. 向Vue原型上存放hello方法（vm和vc就都可以使用了）
        Vue.prototype.hello = () => { alert('你好啊') }
    }
}


