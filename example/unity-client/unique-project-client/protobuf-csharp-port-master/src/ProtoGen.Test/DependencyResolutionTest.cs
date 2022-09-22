#region Copyright notice and license

// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// http://github.com/jskeet/dotnet-protobufs/
// Original C++/Java/Python code:
// http://code.google.com/p/protobuf/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

#endregion

using System.Collections.Generic;
using Google.ProtocolBuffers.DescriptorProtos;
using Google.ProtocolBuffers.Descriptors;
using NUnit.Framework;

namespace Google.ProtocolBuffers.ProtoGen
{
    /// <summary>
    /// Tests for the dependency resolution in Generator.
    /// </summary>
    [TestFixture]
    public class DependencyResolutionTest
    {
        [Test]
        public void TwoDistinctFiles()
        {
            FileDescriptorProto first = new FileDescriptorProto.Builder {Name = "First"}.Build();
            FileDescriptorProto second = new FileDescriptorProto.Builder {Name = "Second"}.Build();
            var set = new List<FileDescriptorProto> { first, second };

            IList<FileDescriptor> converted = Generator.ConvertDescriptors(CSharpFileOptions.DefaultInstance, set);
            Assert.AreEqual(2, converted.Count);
            Assert.AreEqual("First", converted[0].Name);
            Assert.AreEqual(0, converted[0].Dependencies.Count);
            Assert.AreEqual("Second", converted[1].Name);
            Assert.AreEqual(0, converted[1].Dependencies.Count);
        }

        [Test]
        public void FirstDependsOnSecond()
        {
            FileDescriptorProto first =
                new FileDescriptorProto.Builder {Name = "First", DependencyList = {"Second"}}.Build();
            FileDescriptorProto second = new FileDescriptorProto.Builder {Name = "Second"}.Build();
            var set = new List<FileDescriptorProto> { first, second };
            IList<FileDescriptor> converted = Generator.ConvertDescriptors(CSharpFileOptions.DefaultInstance, set);
            Assert.AreEqual(2, converted.Count);
            Assert.AreEqual("First", converted[0].Name);
            Assert.AreEqual(1, converted[0].Dependencies.Count);
            Assert.AreEqual(converted[1], converted[0].Dependencies[0]);
            Assert.AreEqual("Second", converted[1].Name);
            Assert.AreEqual(0, converted[1].Dependencies.Count);
        }

        [Test]
        public void SecondDependsOnFirst()
        {
            FileDescriptorProto first = new FileDescriptorProto.Builder {Name = "First"}.Build();
            FileDescriptorProto second =
                new FileDescriptorProto.Builder {Name = "Second", DependencyList = {"First"}}.Build();
            var set = new List<FileDescriptorProto> { first, second };
            IList<FileDescriptor> converted = Generator.ConvertDescriptors(CSharpFileOptions.DefaultInstance, set);
            Assert.AreEqual(2, converted.Count);
            Assert.AreEqual("First", converted[0].Name);
            Assert.AreEqual(0, converted[0].Dependencies.Count);
            Assert.AreEqual("Second", converted[1].Name);
            Assert.AreEqual(1, converted[1].Dependencies.Count);
            Assert.AreEqual(converted[0], converted[1].Dependencies[0]);
        }

        [Test]
        public void CircularDependency()
        {
            FileDescriptorProto first =
                new FileDescriptorProto.Builder {Name = "First", DependencyList = {"Second"}}.Build();
            FileDescriptorProto second =
                new FileDescriptorProto.Builder {Name = "Second", DependencyList = {"First"}}.Build();
            var set = new List<FileDescriptorProto> { first, second };
            try
            {
                Generator.ConvertDescriptors(CSharpFileOptions.DefaultInstance, set);
                Assert.Fail("Expected exception");
            }
            catch (DependencyResolutionException)
            {
                // Expected
            }
        }

        [Test]
        public void MissingDependency()
        {
            FileDescriptorProto first =
                new FileDescriptorProto.Builder {Name = "First", DependencyList = {"Second"}}.Build();
            var set = new List<FileDescriptorProto> { first };
            try
            {
                Generator.ConvertDescriptors(CSharpFileOptions.DefaultInstance, set);
                Assert.Fail("Expected exception");
            }
            catch (DependencyResolutionException)
            {
                // Expected
            }
        }

        [Test]
        public void SelfDependency()
        {
            FileDescriptorProto first =
                new FileDescriptorProto.Builder {Name = "First", DependencyList = {"First"}}.Build();
            var set = new List<FileDescriptorProto> { first };
            try
            {
                Generator.ConvertDescriptors(CSharpFileOptions.DefaultInstance, set);
                Assert.Fail("Expected exception");
            }
            catch (DependencyResolutionException)
            {
                // Expected
            }
        }
    }
}