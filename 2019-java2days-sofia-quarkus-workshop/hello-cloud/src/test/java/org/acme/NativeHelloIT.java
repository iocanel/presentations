package org.acme;

import io.quarkus.test.junit.SubstrateTest;

@SubstrateTest
public class NativeHelloIT extends HelloTest {

    // Execute the same tests but in native mode.
}