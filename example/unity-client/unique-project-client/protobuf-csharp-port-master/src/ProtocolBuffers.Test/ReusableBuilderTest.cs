﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;
using Google.ProtocolBuffers.Collections;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Google.ProtocolBuffers.TestProtos;
using Google.ProtocolBuffers.Serialization;
using UnitTest.Issues.TestProtos;

namespace Google.ProtocolBuffers
{
    [TestClass]
    public class ReusableBuilderTest
    {
        //Issue 28: Circular message dependencies result in null defaults for DefaultInstance
        [TestMethod]
        public void EnsureStaticCicularReference()
        {
            MyMessageAReferenceB ab = MyMessageAReferenceB.DefaultInstance;
            Assert.IsNotNull(ab);
            Assert.IsNotNull(ab.Value);
            MyMessageBReferenceA ba = MyMessageBReferenceA.DefaultInstance;
            Assert.IsNotNull(ba);
            Assert.IsNotNull(ba.Value);
        }

        [TestMethod]
        public void TestModifyDefaultInstance()
        {
            //verify that the default instance has correctly been marked as read-only
            Assert.AreEqual(typeof(PopsicleList<bool>), TestAllTypes.DefaultInstance.RepeatedBoolList.GetType());
            PopsicleList<bool> list = (PopsicleList<bool>)TestAllTypes.DefaultInstance.RepeatedBoolList;
            Assert.IsTrue(list.IsReadOnly);
        }

        [TestMethod]
        public void TestUnmodifiedDefaultInstance()
        {
            //Simply calling ToBuilder().Build() no longer creates a copy of the message
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void BuildMultipleWithoutChange()
        {
            //Calling Build() or BuildPartial() does not require a copy of the message
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            builder.SetDefaultBool(true);

            TestAllTypes first = builder.BuildPartial();
            //Still the same instance?
            Assert.IsTrue(ReferenceEquals(first, builder.Build()));
            //Still the same instance?
            Assert.IsTrue(ReferenceEquals(first, builder.BuildPartial().ToBuilder().Build()));
        }

        [TestMethod]
        public void MergeFromDefaultInstance()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            builder.MergeFrom(TestAllTypes.DefaultInstance);
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void BuildNewBuilderIsDefaultInstance()
        {
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, new TestAllTypes.Builder().Build()));
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, TestAllTypes.CreateBuilder().Build()));
            //last test, if you clear a builder it reverts to default instance
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance,
                TestAllTypes.CreateBuilder().SetOptionalBool(true).Build().ToBuilder().Clear().Build()));
        }

        [TestMethod]
        public void BuildModifyAndRebuild()
        {
            TestAllTypes.Builder b1 = new TestAllTypes.Builder();
            b1.SetDefaultInt32(1);
            b1.AddRepeatedInt32(2);
            b1.SetOptionalForeignMessage(ForeignMessage.DefaultInstance);

            TestAllTypes m1 = b1.Build();

            b1.SetDefaultInt32(5);
            b1.AddRepeatedInt32(6);
            b1.SetOptionalForeignMessage(b1.OptionalForeignMessage.ToBuilder().SetC(7));

            TestAllTypes m2 = b1.Build();
            
            Assert.AreEqual("{\"optional_foreign_message\":{},\"repeated_int32\":[2],\"default_int32\":1}", Extensions.ToJson(m1));
            Assert.AreEqual("{\"optional_foreign_message\":{\"c\":7},\"repeated_int32\":[2,6],\"default_int32\":5}", Extensions.ToJson(m2));
        }

        [TestMethod]
        public void CloneOnChangePrimitive()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            builder.SetDefaultBool(true);
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnAddRepeatedBool()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            builder.AddRepeatedBool(true);
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnGetRepeatedBoolList()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            GC.KeepAlive(builder.RepeatedBoolList);
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnChangeMessage()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            builder.SetOptionalForeignMessage(new ForeignMessage.Builder());
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnClearMessage()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            builder.ClearOptionalForeignMessage();
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnGetRepeatedForeignMessageList()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            GC.KeepAlive(builder.RepeatedForeignMessageList);
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnChangeEnumValue()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            builder.SetOptionalForeignEnum(ForeignEnum.FOREIGN_BAR);
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

        [TestMethod]
        public void CloneOnGetRepeatedForeignEnumList()
        {
            TestAllTypes.Builder builder = TestAllTypes.DefaultInstance.ToBuilder();
            Assert.IsTrue(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
            GC.KeepAlive(builder.RepeatedForeignEnumList);
            Assert.IsFalse(ReferenceEquals(TestAllTypes.DefaultInstance, builder.Build()));
        }

    }
}
